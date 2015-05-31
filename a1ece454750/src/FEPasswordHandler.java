
package ece454750s15a1;

import org.mindrot.jbcrypt.BCrypt;


public class FEPasswordHandler implements A1Password.Iface {
	
	public FEPasswordHandler() {
	}

	@Override
	public String hashPassword(String password, short logRounds) {
	
		FEServer.print("FE hashPasword");
	
		return "";
	
	}

	@Override
	public boolean checkPassword(String password, String hash) {
	
	
		FEServer.print("FE checkPassword");
	
		return false;
	
	}

}


