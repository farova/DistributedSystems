
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
	
public class Server {
	
	protected static A1Password.Processor m_passwordProcessor;
	protected static A1Management.Processor m_managementProcessor;
	
	protected static String m_host;
	protected static int m_pPort;
	protected static int m_mPort;
	protected static short m_nCores;
	protected static boolean m_debug;
	
	protected static List<Node> m_seedList;
	
	protected static final String m_hostURL = ".uwaterloo.ca";
	
	protected static void parseArgs(String [] args) {
	
		m_debug = false;
		m_seedList = new ArrayList<Node>();
	
		try {
			for(int i = 0; i < args.length; i++) {
				if(args[i].equals("-host")) {
					m_host = args[i+1] + m_hostURL;
				} else if (args[i].equals("-pport")) {
					m_pPort = Integer.parseInt( args[i+1] );
				} else if (args[i].equals("-mport")) {
					m_mPort = Integer.parseInt( args[i+1] );
				} else if (args[i].equals("-ncores")) {
					m_nCores = Short.parseShort( args[i+1] );
				} else if (args[i].equals("-seeds")) {
				
					String[] seedList = args[i+1].split(",");
					for(String seedDataString : seedList)
					{
						String[] seedData = seedDataString.split(":");
						
						m_seedList.add(
							new Node(
								seedData[0] + m_hostURL,
								Integer.parseInt( seedData[1])
							)
						);
					}
				} else if (args[i].equals("-d")) {
					m_debug = true;
				}
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
		
		print(	
			"m_host: " + m_host + 
			" m_mPort: " + m_mPort + 
			" m_pPort: " + m_pPort + 
			" m_nCores: " + m_nCores				
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
			TServerTransport serverTransport = new TServerSocket(m_pPort);
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
			TServerTransport serverTransport = new TServerSocket(m_mPort);
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
}