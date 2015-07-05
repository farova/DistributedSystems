/**
 * ECE 454/750: Distributed Computing
 *
 * Code written by Wojciech Golab, University of Waterloo, 2015
 *
 * IMPLEMENT YOUR SOLUTION IN THIS FILE
 *
 */

package ece454750s15a2;

import java.io.*;
import java.util.*;

public class TriangleCountImpl {
    private byte[] input;
    private int numCores;

    ArrayList<Triangle> returnTriangles = new ArrayList<Triangle>();

    public TriangleCountImpl(byte[] input, int numCores) {
	this.input = input;
	this.numCores = numCores;
    }

    public List<String> getGroupMembers() {
	ArrayList<String> ret = new ArrayList<String>();
	ret.add("mfarova");
	ret.add("n9krishn");
	ret.add("twhart");
	return ret;
    }

    public List<Triangle> enumerateTriangles() throws IOException {

    	final ArrayList<ArrayList<Integer>> adjacencyList = getAdjacencyList(input);

        if(numCores == 1){
                getResultSet(0,adjacencyList.size(), adjacencyList);
        } else {

            int increment = (adjacencyList.size()  / numCores) + 1;

            int beginIndex;
            int endIndex = 0;

            ArrayList<Thread> threads = new ArrayList<Thread>();

            for(int i = 0; i < numCores; i++)
            {
                beginIndex = endIndex;
                endIndex = beginIndex + increment;

                endIndex = endIndex > adjacencyList.size() ? adjacencyList.size() : endIndex;

                final int begin = beginIndex;
                final int end = endIndex;

                Thread t = new Thread(
                    new Runnable() {
                        public void run() {
                            //List<Triangle> result = getResultSet(begin, end, adjacencyList);

                            getResultSet(begin, end, adjacencyList);

                            /*synchronized(result) {
                                returnTriangles.addAll(result);
                            }*/

                        }
                    }
                );

                t.start();
                threads.add(t);
            }

            for(Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {

                }
            }

        }



    	return returnTriangles;
    }

    private void getResultSet(int begin, int end, ArrayList<ArrayList<Integer>> adjacencyList) {
        ArrayList<Integer> n1;
        boolean pass;
        //ArrayList<Triangle> ret = new ArrayList<Triangle>();

        for (int i = begin; i < end; i++) {
            n1 = adjacencyList.get(i);
            if (n1.size() < 2) {
                continue;
            }
            Set<Integer> hashSet = new HashSet<Integer> (n1);
            for (Integer j : n1) {
                if (j > i) {
                    ArrayList<Integer> n2 = adjacencyList.get(j);
                    for (Integer k : n2) {
                        if (k > j && hashSet.contains(k)) {
                            //ret.add(new Triangle(i,j,k));

                            do { 
                                pass = returnTriangles.add(new Triangle(i,j,k));
                            } while( !pass );


                        }
                    }
                }
            }
        }

        //return ret;
    }


    public ArrayList<ArrayList<Integer>> getAdjacencyList(byte[] data) throws IOException {
	InputStream istream = new ByteArrayInputStream(data);
	BufferedReader br = new BufferedReader(new InputStreamReader(istream));
	String strLine = br.readLine();
	if (!strLine.contains("vertices") || !strLine.contains("edges")) {
	    System.err.println("Invalid graph file format. Offending line: " + strLine);
	    System.exit(-1);
	}
	String parts[] = strLine.split(", ");
	int numVertices = Integer.parseInt(parts[0].split(" ")[0]);
	int numEdges = Integer.parseInt(parts[1].split(" ")[0]);
	System.out.println("Found graph with " + numVertices + " vertices and " + numEdges + " edges");

	ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<ArrayList<Integer>>(numVertices);
	for (int i = 0; i < numVertices; i++) {
	    adjacencyList.add(new ArrayList<Integer>());
	}
	while ((strLine = br.readLine()) != null && !strLine.equals(""))   {
	    parts = strLine.split(": ");
	    int vertex = Integer.parseInt(parts[0]);
	    if (parts.length > 1) {
		parts = parts[1].split(" +");
		for (String part: parts) {
		    adjacencyList.get(vertex).add(Integer.parseInt(part));
		}
	    }
	}
	br.close();
	return adjacencyList;
    }
}
