
package ece454750s15a1;

import org.mindrot.jbcrypt.BCrypt;

public class BEPasswordHandler implements A1Password.Iface {
	
	public BEPasswordHandler() {
	}

	@Override
	public String hashPassword(String password, short logRounds) {
		return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
	}

	@Override
	public boolean checkPassword(String password, String hash) {
		return BCrypt.checkpw(password, hash);
	}

}

