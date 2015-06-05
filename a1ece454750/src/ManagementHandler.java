
package ece454750s15a1;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ManagementHandler {

	protected boolean m_isAcked;
	private long m_startTime;
	
	private AtomicInteger m_numRequestsReceived;
	private AtomicInteger m_numRequestsCompleted;
	
	public ManagementHandler() {
		m_isAcked = false;
		m_startTime = System.nanoTime();
		
		m_numRequestsReceived = new AtomicInteger();
		m_numRequestsCompleted = new AtomicInteger();
		
		m_numRequestsReceived.set(0);
		m_numRequestsCompleted.set(0);
	}
	
	private int getSecondsUp() {
		long elapsedTime = System.nanoTime() - m_startTime;
		return (int)(elapsedTime / 1E9);
	}
	
	public boolean isAcked() {
		return m_isAcked;
	}
	
	protected List<String> getGroupMembersList() {
		return Arrays.asList("mfarova", "n9krishn");
	}
	
	protected PerfCounters getPerfCounterObject() {
		PerfCounters counters = new PerfCounters();
		counters.numSecondsUp = getSecondsUp();
		counters.numRequestsReceived = getNumRequestsReceived();
		counters.numRequestsCompleted = getNumRequestsCompleted();
		
		return counters;
	}
	
	private int getNumRequestsReceived() {
		return m_numRequestsReceived.get();
	}
	
	private int getNumRequestsCompleted() {
		return m_numRequestsCompleted.get();
	}
	
	public void incrementNumRequestsReceived() {
		m_numRequestsReceived.incrementAndGet();
	}
	
	public void incrementNumRequestsCompleted() {
		m_numRequestsCompleted.incrementAndGet();
	}
}