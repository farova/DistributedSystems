
package ece454750s15a1;

public class BEServer extends Server{

	public static BEPasswordHandler m_passwordHandler;
	public static BEManagementHandler m_managementHandler;

	public static void main(String [] args) {
		parseArgs(args);

		m_managementHandler = new BEManagementHandler();
		m_managementProcessor = new A1Management.Processor(m_managementHandler);
		
		m_passwordHandler = new BEPasswordHandler(m_managementHandler);
		m_passwordProcessor = new A1Password.Processor(m_passwordHandler);

		startServiceThreads();
		
		while(!m_managementHandler.isAcked()) {
			joinFESeed(true);
		}
	}
}
