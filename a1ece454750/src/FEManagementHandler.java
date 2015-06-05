
package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class FEManagementHandler extends ManagementHandler implements A1Management.Iface {
	
	private CopyOnWriteArrayList<NodeData> m_FEnodesList;
	private CopyOnWriteArrayList<NodeData> m_BEnodesList;
	
	private Timer m_timer;
	
	private String m_host;
	private int m_mport;
	private int m_pport;
	private int m_ncores;
	
	public FEManagementHandler(String host, int mport, int pport, short ncores) {
		
		m_host = host;
		m_mport = mport;
		m_pport = pport;
		m_ncores = ncores;
		
		m_FEnodesList = new CopyOnWriteArrayList<NodeData>();
		m_BEnodesList = new CopyOnWriteArrayList<NodeData>();
		
		// Add self to FE nodes list
		m_FEnodesList.add(new NodeData(host, pport, mport, ncores));
		
		m_timer = new Timer();
		
		// Set up timerTask for use in gossip protocol ever 100ms starting 100ms after startup
		m_timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				gossip();
			}
		}, 100, 100);
	}

	@Override
	public PerfCounters getPerfCounters() {
		PerfCounters counters = new PerfCounters();
		
		counters.numSecondsUp = getSecondsUp();
		
		
		counters.numRequestsReceived = 0;
		counters.numRequestsCompleted = 0;
		
		return counters;
	}

	@Override
	public List<String> getGroupMembers() throws TException {
		return Arrays.asList("mfarova", "n9krishn");
	}
	
	@Override
	public void joinRequest(JoinRequestData data) { 
		FEServer.print("Request from " + data.host + ":" + data.mport);
		
		if(data.isBE) {
			m_BEnodesList.add(new NodeData(data.host, data.pport, data.mport, data.ncores));
			printBEnodesList();
		} else {
			m_FEnodesList.add(new NodeData(data.host, data.pport, data.mport, data.ncores));
			printFEnodesList();
		}
		
		sendJoinAck(data.host, data.mport);
		
	}
	
	@Override
	public void joinAck(JoinAckData data) {
		m_isAcked = data.isAcked;
	}
	
	@Override
	public void recieveGossip(GossipData gossipData) {
		
		//FEServer.print("Old BE node list:");
		//printBEnodesList();
		
		for(NodeData data : gossipData.BEnodes) {
			m_BEnodesList.addIfAbsent(data);
		}
		
		//FEServer.print("New BE node list:");
		//printBEnodesList();
		
		
		//FEServer.print("Old FE node list:");
		//printFEnodesList();
		
		for(NodeData data : gossipData.FEnodes) {
			m_FEnodesList.addIfAbsent(data);
		}
		
		//FEServer.print("New FE node list:");
		//printFEnodesList();
		
	}
	
	public void gossip() {
		GossipData gossipData = new GossipData();
		
		gossipData.BEnodes = m_BEnodesList;
		gossipData.FEnodes = m_FEnodesList;
		
		List<NodeData> unreachableNodes = new ArrayList<NodeData>();
		
		
		for(NodeData node : m_FEnodesList) {
			if(node.host != m_host && node.mport != m_mport) {
				try {
					TTransport transport;
					transport = new TSocket(node.host, node.mport);
					transport.open();
					
					TProtocol protocol = new  TBinaryProtocol(transport);
					A1Management.Client FEmanagement = new A1Management.Client(protocol);
					
					//FEServer.print("Sending Gossip");
					FEmanagement.recieveGossip(gossipData);
		
					transport.close();
				} catch (TException x) {
					unreachableNodes.add(node);
				}
			}
		}
		
		m_FEnodesList.removeAll(unreachableNodes);
		
		//FEServer.print("New FE node list:");
		//printFEnodesList();
	}
	
	public void removeUnreachableBE(NodeData node) {
		m_BEnodesList.remove(node);
	}
	
	public void removeUnreachableFE(NodeData node) {
		m_FEnodesList.remove(node);
	}
	
	public NodeData getBestBE() {
		
		if(m_BEnodesList.size() == 0) {
			return null;
		}
		
		// Get total weight
		int totalWeight = 0;
		Iterator<NodeData> iterator = m_BEnodesList.iterator(); 
		while (iterator.hasNext()) {
			NodeData data = iterator.next();
			totalWeight += data.ncores;
		}
		
		// Choose random node based on nCores
		Random randomizer = new Random();
		int weight = (int)randomizer.nextInt(totalWeight + 1);
		
		iterator = m_BEnodesList.iterator(); 
		while (iterator.hasNext()) {
			NodeData data = iterator.next();
			weight -= data.ncores;
			
			if(weight <= 0) {
				return data;
			}
		}
		
		FEServer.print("This shouldn't happen!");
		return m_BEnodesList.get(0);
	}
	
	public void printFEnodesList() {
		FEServer.print("Current FE nodes connected:");
		printNodeList(m_FEnodesList);
	}
	
	public void printBEnodesList() {
		FEServer.print("Current BE nodes connected:");
		printNodeList(m_BEnodesList);
	}
	
	private void printNodeList(CopyOnWriteArrayList<NodeData> list) {
		Iterator<NodeData> iterator = list.iterator(); 
		while (iterator.hasNext()) {
			NodeData data = iterator.next();
			FEServer.print("- " + data.host + ":" + data.pport);
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


