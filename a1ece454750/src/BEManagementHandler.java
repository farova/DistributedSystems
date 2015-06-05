
package ece454750s15a1;

import org.apache.thrift.TException;
import java.util.List;

public class BEManagementHandler extends ManagementHandler implements A1Management.Iface {
	
	public BEManagementHandler() {}

	@Override
	public PerfCounters getPerfCounters() {
		return getPerfCounterObject();
	}

	@Override
	public List<String> getGroupMembers() throws TException {
		return getGroupMembersList();
	}

	@Override
	public void joinRequest(JoinRequestData data) { 
		
	}
	
	@Override
	public void joinAck(JoinAckData data) {
		BEServer.print("ACKED!");
		m_isAcked = data.isAcked;
	}
	
	@Override
	public void recieveGossip(GossipData gossipData) {
		
		FEServer.print("BAD GOSSIP:");
	}
}


