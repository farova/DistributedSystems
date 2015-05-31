
package ece454750s15a1;

import org.apache.thrift.TException;
import java.util.List;
import java.util.Arrays;

public class BEManagementHandler implements A1Management.Iface {
	
	PerfCounters m_counters;
	
	private boolean m_isAcked;
	private long m_startTime;
	
	public BEManagementHandler() {
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
		
	}
	
	@Override
	public void joinAck(JoinAckData data) {
		
	}
	
	public boolean isAcked() {
		return m_isAcked;
	}
	
}


