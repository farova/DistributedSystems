
package ece454750s15a1;

public class FEServer extends Server {

	public static FEPasswordHandler m_passwordHandler;
	public static FEManagementHandler m_managementHandler;

	public static void main(String [] args) {
		parseArgs(args);

		m_managementHandler = new FEManagementHandler(m_host, m_mport, m_pport, m_ncores);
		m_managementProcessor = new A1Management.Processor(m_managementHandler);
		
		m_passwordHandler = new FEPasswordHandler(m_managementHandler);
		m_passwordProcessor = new A1Password.Processor(m_passwordHandler);

		startServiceThreads();
		
		
		for(int i = 0; i < getNumSeeds(); i++) {
			if(!m_managementHandler.isAcked()) {
				NodeData seed = m_seedList.get(i);
				
				if(seed.host != m_host && seed.mport != m_mport) {
					joinFESeed(false, seed, false);
				}
			} else {
				break;
			}
		}
		
		if(!m_managementHandler.isAcked()) {
			print("FE node not ACKed! Was it the first node up?");
		}
		
		
	}
}
