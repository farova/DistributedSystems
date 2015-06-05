
package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class TestClient extends Server{

	public static void main(String [] args) {
		parseArgs(args);

		try {
			TTransport transport;
			transport = new TSocket(m_host, m_pport);
			transport.open();

			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Password.Client passClient = new A1Password.Client(protocol);

			perform(passClient);

			transport.close();
		} catch (TException x) {
			x.printStackTrace();
		} 
	}

	private static void perform(A1Password.Client passClient) throws TException
	{
		String password = "test";
		
		System.out.println("Doing forward hash: ");
		String hash = passClient.hashPassword(password, (short)20);
		System.out.println("Password: " + password + " Hash: " + hash);
		
		System.out.println("Doing backwards hash: ");
		boolean correctPass = passClient.checkPassword(password, hash);
		System.out.println("Is password correct: " + correctPass);
	}
}
