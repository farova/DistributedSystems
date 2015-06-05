
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
	private boolean m_throwServiceUnavailableException;
	
	public FEPasswordHandler(FEManagementHandler managementHandler) {
		m_managementHandler = managementHandler;
		m_timer = new Timer();
		m_throwServiceUnavailableException = false;
	}

	@Override
	public String hashPassword(String password, short logRounds) throws ServiceUnavailableException {
	
		FEServer.print("FE hashPasword");
	
		m_managementHandler.incrementNumRequestsReceived();
	
		String hashedPass = "";
		
		boolean timerInitialized = false;
		
		while(true){
			NodeData data = m_managementHandler.getBestBE();
			
			if(data == null) {
				if(!timerInitialized) {
					startTimer();
					timerInitialized = true;
				}
			} else {			
				try {
					TTransport transport;
					transport = new TSocket(data.host, data.pport);
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
			
			if(timerInitialized && m_throwServiceUnavailableException) {
				throw new ServiceUnavailableException();
			}
		}
	
		m_managementHandler.incrementNumRequestsCompleted();
	
		return hashedPass;
	}

	@Override
	public boolean checkPassword(String password, String hash) throws ServiceUnavailableException {
	
		FEServer.print("FE checkPassword");
		
		m_managementHandler.incrementNumRequestsReceived();
		
		boolean correctHash = false;
		
		boolean timerInitialized = false;
		
		while(true){
			NodeData data = m_managementHandler.getBestBE();
			
			if(data == null) {
				if(!timerInitialized) {
					startTimer();
					timerInitialized = true;
				}
			} else {
				try {
					TTransport transport;
					transport = new TSocket(data.host, data.pport);
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
					m_managementHandler.removeUnreachableBE(data);
				}
			}
				
			if(timerInitialized && m_throwServiceUnavailableException) {
				throw new ServiceUnavailableException();
			}
		}
		
		m_managementHandler.incrementNumRequestsCompleted();
		
		return correctHash;
	}
	
	private void startTimer() {
		m_timer.schedule(new TimerTask() {
			@Override
			public void run() {
				m_throwServiceUnavailableException = true;
			}
		}, 60000);
		FEServer.print("Staring timer");
	}
	
	private void stopTimer() {
		m_timer.cancel();
		m_timer.purge();
		FEServer.print("Ending timer");
	}

}


