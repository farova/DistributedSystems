
package ece454750s15a1;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import java.util.Timer;
import java.util.TimerTask;

public class FEPasswordHandler implements A1Password.Iface {
	
	FEManagementHandler m_managementHandler;
	
	private Timer m_timer;
	
	public FEPasswordHandler(FEManagementHandler managementHandler) {
		m_managementHandler = managementHandler;
		m_timer = new Timer();
	}

	@Override
	public String hashPassword(String password, short logRounds) {
	
		FEServer.print("FE hashPasword");
		
		String hashedPass = "";
		
		while(true){
			Node data = m_managementHandler.getBestBE();
			
			if(data == null) {
				if(!timerInitialized) {
					startTimer();
					timerInitialized = true;
				}
			}
			
			try {
				TTransport transport;
				transport = new TSocket(data.m_host, data.m_pport);
				transport.open();

				TProtocol protocol = new  TBinaryProtocol(transport);
				A1Password.Client BEpass = new A1Password.Client(protocol);
				
				hashedPass = BEpass.hashPassword(password, logRounds);

				transport.close();
				
				if(timerInitialized) {
					stopTimer();
				}
				
				break;
			} catch (TException x) {
				if(data != null) {
					m_managementHandler.removeUnreachableBE(data);
				}
			}
		}
	
		return hashedPass;
	}

	@Override
	public boolean checkPassword(String password, String hash) {
	
		FEServer.print("FE checkPassword");
		
		boolean correctHash = false;
		
		boolean timerInitialized = false;
		
		while(true){
			Node data = m_managementHandler.getBestBE();
			
			if(data == null) {
				if(!timerInitialized) {
					startTimer();
					timerInitialized = true;
				}
			}
			
			try {
				TTransport transport;
				transport = new TSocket(data.m_host, data.m_pport);
				transport.open();

				TProtocol protocol = new  TBinaryProtocol(transport);
				A1Password.Client BEpass = new A1Password.Client(protocol);
				
				correctHash = BEpass.checkPassword(password, hash);

				transport.close();
				
				if(timerInitialized) {
					stopTimer();
				}
				
				break;
			} catch (TException x) {
				if(data != null) {
					m_managementHandler.removeUnreachableBE(data);
				}
			}
		}
		
		return correctHash;
	}
	
	private void startTimer() {
		m_timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				throw new ServiceUnavailableException();
			}
		}, 0, 60000);
	}
	
	private void stopTimer() {
		m_timer.cancel();
		m_timer.purge();
	}

}


