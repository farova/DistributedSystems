
import org.apache.thrift.TException;

import ece454750s15a1.*;

import java.util.List;
import java.util.Arrays;

public class FEManagementHandler implements A1Management.Iface {
	
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

}


