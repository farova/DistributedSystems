
package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
	
public class Server {
	
	protected static A1Password.Processor m_passwordProcessor;
	protected static A1Management.Processor m_managementProcessor;
	
	protected static String m_host;
	protected static int m_pport;
	protected static int m_mport;
	protected static short m_ncores;
	protected static boolean m_debug;
	
	protected static List<NodeData> m_seedList;
	
	protected static final String m_hostURL = ".uwaterloo.ca";
	
	protected static int getNumSeeds() {
		return m_seedList.size();
	}
	
	protected static void parseArgs(String [] args) {
	
		m_debug = false;
		m_seedList = new ArrayList<NodeData>();
	
		try {
			for(int i = 0; i < args.length; i++) {
				if(args[i].equals("-host")) {
					m_host = args[i+1] + m_hostURL;
				} else if (args[i].equals("-pport")) {
					m_pport = Integer.parseInt( args[i+1] );
				} else if (args[i].equals("-mport")) {
					m_mport = Integer.parseInt( args[i+1] );
				} else if (args[i].equals("-ncores")) {
					m_ncores = Short.parseShort( args[i+1] );
				} else if (args[i].equals("-seeds")) {
				
					String[] seedList = args[i+1].split(",");
					for(String seedDataString : seedList)
					{
						String[] seedData = seedDataString.split(":");
						
						m_seedList.add(
							new NodeData(
								seedData[0] + m_hostURL,
								0,
								Integer.parseInt( seedData[1]),
								(short)0
							)
						);
					}
				} else if (args[i].equals("-d")) {
					m_debug = true;
				}
			}
		} catch (Exception x) {
			print("Bad arguments!");
		}
		
		print(	
			"m_host: " + m_host + 
			" m_mport: " + m_mport + 
			" m_pport: " + m_pport + 
			" m_ncores: " + m_ncores				
		);
	}
	
	protected static void startServiceThreads() {
		try {
			new Thread(getPasswordRunnable()).start();
			new Thread(getManagementRunnable()).start();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	
	private static Runnable getPasswordRunnable() {
		return new Runnable() {
			public void run() {
				runPasswordService(m_passwordProcessor);
			}
		}; 
	}

	private static Runnable getManagementRunnable() {
		return new Runnable() {
			public void run() {
				runManagementService(m_managementProcessor);
			}
		}; 
	}

	private static void runPasswordService(A1Password.Processor processor) {
		try {
			TServerTransport serverTransport = new TServerSocket(m_pport);
			TServer server = new TSimpleServer(
				new Args(serverTransport).processor(processor)
			);

			print("Starting the PasswordService");
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void runManagementService(A1Management.Processor processor) {
		try {
			TServerTransport serverTransport = new TServerSocket(m_mport);
			TServer server = new TSimpleServer(
				new Args(serverTransport).processor(processor)
			);

			print("Starting the ManagementService");
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void print(String msg) {
		if(m_debug) {
			System.out.println(msg);
		}
	}
	
	
	protected static NodeData getRandomFESeed() {
		Random randomizer = new Random();
		return m_seedList.get(randomizer.nextInt(m_seedList.size()));
	}
	
	protected static void joinFESeed(boolean isBE) {
		joinFESeed(isBE, getRandomFESeed(), true);
	}
	
	protected static void joinFESeed(boolean isBE, NodeData seed, boolean retryWithRandom) {
	
		//Generate join request data
		JoinRequestData request = new JoinRequestData();
		request.host = m_host;
		request.pport = m_pport;
		request.mport = m_mport;
		request.ncores = m_ncores;
		request.isBE = isBE;
		
		do {
			try {
				TTransport transport;
				transport = new TSocket(seed.host, seed.mport);
				transport.open();

				TProtocol protocol = new  TBinaryProtocol(transport);
				A1Management.Client FEmanagement = new A1Management.Client(protocol);

				// Try join server
				print("Sending request to FE node to " + seed.host + ":" + seed.mport);
				FEmanagement.joinRequest(request);

				transport.close();
				
				break;
			} catch (TException x) {
				seed = getRandomFESeed();
			} 
		} while(retryWithRandom);
	}
}