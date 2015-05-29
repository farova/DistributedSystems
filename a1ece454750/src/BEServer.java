
package ece454750s15a1;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class BEServer extends Server{

	public static BEPasswordHandler m_passwordHandler;
	public static A1Password.Processor m_passwordProcessor;
	
	public static BEManagementHandler m_managementHandler;
	public static A1Management.Processor m_managementProcessor;

	public static void main(String [] args) {
		parseArgs(args);
	
		try {
			m_passwordHandler = new BEPasswordHandler();
			m_passwordProcessor = new A1Password.Processor(m_passwordHandler);

			m_managementHandler = new BEManagementHandler();
			m_managementProcessor = new A1Management.Processor(m_managementHandler);

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

	public static void runPasswordService(A1Password.Processor processor) {
		try {
			TServerTransport serverTransport = new TServerSocket(m_pPort);
			TServer server = new TSimpleServer(
				new Args(serverTransport).processor(processor)
			);

			System.out.println("Starting the PasswordService");
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void runManagementService(A1Management.Processor processor) {
		try {
			TServerTransport serverTransport = new TServerSocket(m_mPort);
			TServer server = new TSimpleServer(
				new Args(serverTransport).processor(processor)
			);

			System.out.println("Starting the ManagementService");
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
