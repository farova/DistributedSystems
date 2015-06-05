
package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class TestPerfCounters extends Server{

	public static void main(String [] args) {
		parseArgs(args);

		try {
			TTransport transport;
			transport = new TSocket(m_host, m_mport);
			transport.open();

			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Management.Client client = new A1Management.Client(protocol);

			
			PerfCounters counters = client.getPerfCounters();
			System.out.println("Perf Counters: ");
			System.out.println(" - numSecondsUp: " + counters.numSecondsUp);
			System.out.println(" - numRequestsReceived: " + counters.numRequestsReceived);
			System.out.println(" - numRequestsCompleted: " + counters.numRequestsCompleted);
			
			
			
			transport.close();
		} catch (ServiceUnavailableException x) {
			System.out.println("ServiceUnavailableException!");
		} catch (TException x) {
			x.printStackTrace();
		}
		
	}
}
