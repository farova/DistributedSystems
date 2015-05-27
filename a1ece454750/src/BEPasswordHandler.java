
import org.mindrot.jbcrypt.BCrypt;
import ece454750s15a1.*;

public class BEPasswordHandler implements A1Password.Iface {
	
	public BEPasswordHandler() {}

	@Override
	public String hashPassword(String password, short logRounds) {
		return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
	}

	@Override
	public boolean checkPassword(String password, String hash) {
		return BCrypt.checkpw(password, hash);
	}

}


