
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

public class BEServer extends Server{

	public static BEPasswordHandler m_passwordHandler;
	public static BEManagementHandler m_managementHandler;

	public static void main(String [] args) {
		parseArgs(args);

		m_passwordHandler = new BEPasswordHandler();
		m_passwordProcessor = new A1Password.Processor(m_passwordHandler);

		m_managementHandler = new BEManagementHandler();
		m_managementProcessor = new A1Management.Processor(m_managementHandler);
		
		startServiceThreads();
		
		while(!m_managementHandler.isAcked()) {
			joinFESeed();
			//Thread.sleep(5); 
		}
	}
	
	private static void joinFESeed() {
		try {
			
			//Generate join request data
			JoinRequestData request = new JoinRequestData();
			request.host = m_host;
			request.pport = m_pPort;
			request.mport = m_mPort;
			request.ncores = m_nCores;
			
			//Get random Seed to join
			Seed seed = m_seedList.get(0);
		
			TTransport transport;
			transport = new TSocket(seed.m_host, seed.m_port);
			transport.open();

			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Management.Client FEmanagement = new A1Management.Client(protocol);

			// Try join server
			
			print("Sending request to FE node to " + seed.m_host + ":" + seed.m_port);
			FEmanagement.joinRequest(request);

			transport.close();
		} catch (TException x) {
			x.printStackTrace();
		} 
	}
}
