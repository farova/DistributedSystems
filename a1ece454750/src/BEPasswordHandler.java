
package ece454750s15a1;

import org.mindrot.jbcrypt.BCrypt;

public class BEPasswordHandler implements A1Password.Iface {
	
	BEManagementHandler m_managementHandler;
	
	public BEPasswordHandler(BEManagementHandler managementHandler) {
		m_managementHandler = managementHandler;
	}

	@Override
	public String hashPassword(String password, short logRounds) {
	
		BEServer.print("BE hashPassword");
	
		m_managementHandler.incrementNumRequestsReceived();
		
		String result = BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
		
		m_managementHandler.incrementNumRequestsCompleted();
		
		return result;
	}

	@Override
	public boolean checkPassword(String password, String hash) {
	
		BEServer.print("BE checkPassword");
		
		m_managementHandler.incrementNumRequestsReceived();
		
		boolean result = BCrypt.checkpw(password, hash);
		
		m_managementHandler.incrementNumRequestsCompleted();
		
		return result;
	}

}


