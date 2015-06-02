
package ece454750s15a1;

public class ManagementHandler {

	protected boolean m_isAcked;
	protected long m_startTime;
	
	public ManagementHandler() {
		m_isAcked = false;
		m_startTime = System.nanoTime();
	}
	
	protected int getSecondsUp() {
		long elapsedTime = System.nanoTime() - m_startTime;
		return (int)(elapsedTime / 1E9);
	}
	
	public boolean isAcked() {
		return m_isAcked;
	}
}