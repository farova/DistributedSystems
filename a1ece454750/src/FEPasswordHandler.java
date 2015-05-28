
import org.mindrot.jbcrypt.BCrypt;
import ece454750s15a1.*;


public class FEPasswordHandler implements A1Password.Iface {
	
	public FEPasswordHandler() {}

	@Override
	public String hashPassword(String password, short logRounds) {
		return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
	}

	@Override
	public boolean checkPassword(String password, String hash) {
		return BCrypt.checkpw(password, hash);
	}

}


