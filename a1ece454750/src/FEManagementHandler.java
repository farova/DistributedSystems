
package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;


import java.util.List;
import java.util.Arrays;
//import java.util.concurrent.*;

public class FEManagementHandler implements A1Management.Iface {
	
	//CopyOnWriteArrayList<>
	
	PerfCounters m_counters;
	
	private long m_startTime;
	
	public FEManagementHandler() {
		
		m_counters = new PerfCounters();
		
		m_counters.numSecondsUp = 0;
		m_counters.numRequestsReceived = 0;
		m_counters.numRequestsCompleted = 0;
		
		m_startTime = System.nanoTime();
	}

	@Override
	public PerfCounters getPerfCounters() {
		// Calculate current uptime
		long elapsedTime = System.nanoTime() - m_startTime;
		m_counters.numSecondsUp = (int)(elapsedTime / 1E9);
		
		return m_counters;
	}

	@Override
	public List<String> getGroupMembers() throws TException {
		return Arrays.asList("mfarova", "n9krishn");
	}
	
	@Override
	public void joinRequest(JoinRequestData data) { 
		FEServer.print("Request from " + data.host + ":" + data.mport);
		
		sendJoinAck(data.host, data.mport);
	}
	
	@Override
	public void joinAck(JoinAckData data) {
		
	}
	
	private void sendJoinAck(String host, int port) {
		
		JoinAckData data = new JoinAckData();
		data.isAcked = true;
		
		try {
			TTransport transport;
			transport = new TSocket(host, port);
			transport.open();

			TProtocol protocol = new  TBinaryProtocol(transport);
			A1Management.Client BEmanagement = new A1Management.Client(protocol);
			
			FEServer.print("Sending ACK");
			BEmanagement.joinAck(data);

			transport.close();
		} catch (org.apache.thrift.transport.TTransportException x) {
			x.printStackTrace();
			
			FEServer.print("Type: " + x.getType() );
		} catch (TException x) {
			x.printStackTrace();
		}
	}
	
}


