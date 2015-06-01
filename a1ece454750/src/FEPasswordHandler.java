
package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class FEPasswordHandler implements A1Password.Iface {
	
	FEManagementHandler m_managementHandler;
	
	public FEPasswordHandler(FEManagementHandler managementHandler) {
		m_managementHandler = managementHandler;
	}

	@Override
	public String hashPassword(String password, short logRounds) {
	
		FEServer.print("FE hashPasword");
	
		Node data = m_managementHandler.getBestBE();
		
		String hashedPass = "";
	
		try {
			TTransport transport;
			transport = new TSocket(data.m_host, data.m_pport);
			transport.open();

			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Password.Client BEpass = new A1Password.Client(protocol);
			
			hashedPass = BEpass.hashPassword(password, logRounds);

			transport.close();
		} catch (TException x) {
			x.printStackTrace();
		}
	
		return hashedPass;
	}

	@Override
	public boolean checkPassword(String password, String hash) {
	
		FEServer.print("FE checkPassword");
		
		Node data = m_managementHandler.getBestBE();
		
		boolean correctHash = false;
	
		try {
			TTransport transport;
			transport = new TSocket(data.m_host, data.m_pport);
			transport.open();

			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Password.Client BEpass = new A1Password.Client(protocol);
			
			correctHash = BEpass.checkPassword(password, hash);

			transport.close();
		} catch (TException x) {
			x.printStackTrace();
		}
		
		return correctHash;
	}

}


