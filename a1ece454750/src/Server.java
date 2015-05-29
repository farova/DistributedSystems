
package ece454750s15a1;

import java.util.ArrayList;
import java.util.List;
	
public class Server {
	
	public static String m_host;
	public static int m_pPort;
	public static int m_mPort;
	public static int m_nCores;
	
	public static List<Seed> m_seedList;
	
	protected static String m_hostURL = ".uwaterloo.ca";
	
	public static void parseArgs(String [] args) {
	
		m_seedList = new ArrayList<Seed>();
	
		try {
			for(int i = 0; i < args.length; i++) {
				if(args[i].equals("-host")) {
					m_host = args[i+1] + m_hostURL;
				} else if (args[i].equals("-pport")) {
					m_pPort = Integer.parseInt( args[i+1] );
				} else if (args[i].equals("-mport")) {
					m_mPort = Integer.parseInt( args[i+1] );
				} else if (args[i].equals("-ncores")) {
					m_nCores = Integer.parseInt( args[i+1] );
				} else if (args[i].equals("-seeds")) {
				
					String[] seedList = args[i+1].split(",");
					for(String seedDataString : seedList)
					{
						String[] seedData = seedDataString.split(":");
						
						m_seedList.add(
							new Seed(
								seedData[0] + m_hostURL, 
								Integer.parseInt( seedData[1])
							)
						);
					}
				}
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
		
		/*System.out.println(	
			"m_host: " + m_host + 
			" m_pPort: " + m_pPort + 
			" m_mPort: " + m_mPort + 
			" m_nCores: " + m_nCores				
		);*/
	}

}