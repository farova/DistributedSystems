
import ece454750s15a1.*;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class TestClient {
	public static void main(String [] args) {

		// 1st args is "simple", 2nd args is server address
		/*if (args.length != 2 || !args[0].contains("simple")) {
			System.out.println("Please enter 'simple' ");
			System.exit(0);
		}*/

		try {
			TTransport transport;
			transport = new TSocket("eceubuntu.uwaterloo.ca", 9771);
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
		
		String hash = passClient.hashPassword(password, (short)10);
		
		System.out.println("Password: " + password + " Hash: " + hash);
		
	}
}
