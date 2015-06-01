
package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class FEServer extends Server{

	public static FEPasswordHandler m_passwordHandler;
	public static FEManagementHandler m_managementHandler;

	public static void main(String [] args) {
		parseArgs(args);

		m_managementHandler = new FEManagementHandler();
		m_managementProcessor = new A1Management.Processor(m_managementHandler);
		
		m_passwordHandler = new FEPasswordHandler(m_managementHandler);
		m_passwordProcessor = new A1Password.Processor(m_passwordHandler);

		startServiceThreads();
	}
}
