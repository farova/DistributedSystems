
package ece454750s15a1;

import org.apache.thrift.TException;
import java.util.List;
import java.util.Arrays;

public class BEManagementHandler extends ManagementHandler implements A1Management.Iface {
	
	public BEManagementHandler() {}

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
		
	}
	
	@Override
	public void joinAck(JoinAckData data) {
		BEServer.print("ACKED!");
		m_isAcked = data.isAcked;
	}
}


