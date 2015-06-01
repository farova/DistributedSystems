
package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class FEManagementHandler implements A1Management.Iface {
	
	private CopyOnWriteArrayList<Node> m_FEnodesList;
	
	private PerfCounters m_counters;
	
	private long m_startTime;
	
	public FEManagementHandler() {
		
		m_FEnodesList = new CopyOnWriteArrayList<Node>();
		
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
		
		m_FEnodesList.add(new Node(data.host, data.pport, data.mport, data.ncores));
		
		sendJoinAck(data.host, data.mport);
		
		printFEnodesList();
	}
	
	@Override
	public void joinAck(JoinAckData data) {
		
	}
	
	public Node getBestBE() {
	
	
	
	
	
	
	
	
		//Get random Seed to join
		Random randomizer = new Random();
		return m_FEnodesList.get(randomizer.nextInt(m_FEnodesList.size()));
	}
	
	public void printFEnodesList() {
		FEServer.print("Current BE nodes connected:");
	
		Iterator<Node> iterator = m_FEnodesList.iterator(); 
		while (iterator.hasNext()) {
			Node data = iterator.next();
			FEServer.print("- " + data.m_host + ":" + data.m_pport);
		}
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
		} catch (TException x) {
			x.printStackTrace();
		}
	}
	
}


