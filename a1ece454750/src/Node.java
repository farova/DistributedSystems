
package ece454750s15a1;

public class Node {

	public Node(String host, int mport) {
		this(host, 0, mport, (short)0, true);
	}
	
	public Node(String host, int pport, int mport, short ncores) {
		this(host, pport, mport, ncores, false);
	}
	
	public Node(String host, int pport, int mport, short ncores, boolean isSeed) {
		m_host = host;
		m_pport = pport;
		m_mport = mport;
		m_ncores = ncores;
		m_isSeed = isSeed;
	}

	public String m_host;
	public int m_pport;
	public int m_mport;
	public short m_ncores;
	public boolean m_isSeed;
}