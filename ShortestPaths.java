package codes;

/*
 * Nathan Marcotte
 * V00876934
 * CSC 226 A3
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Iterator;

class Edge implements Comparable<Edge> { //from assignment 2 
	private int v;
	private int w;
    int weight;

	public Edge(int v, int w, int weight) {
		this.v=v;
		this.w=w;
		this.weight=weight;
		}
	
	public int compareTo(Edge other) {
		if(this.weight < other.weight) {return -1;}
		else if (this.weight > other.weight) {return 1;}
		else	{return 0;}
	}
	
	int end1() {return v;}
	int end2(int vertex) {
		if(vertex == v) {return w;}
		else {return v;}
	}
	
	
	}

class vKey implements Comparable<vKey>{
	Integer v;
	Integer key;
	
	public vKey (int v, int key) {
		this.v=v;
		this.key=key;
	}
	
	@Override
	public int compareTo(vKey other){
		
		return this.v.compareTo(other.v);
	} 
	
	
}
	
public class ShortestPaths{
	static PriorityQueue<vKey> minPQ;
	static int[] dist;
	static Edge [] edgeTo;
	

 public static int n; // number of vertices

 static void ShortestPaths(int[][][] adj, int source){
	n = adj.length;;
	minPQ = new PriorityQueue<vKey>(n);
	dist = new int[n];
	edgeTo = new Edge[n];
	vKey sour = new vKey(source,0);
	 
	for(int i = 0; i<n; i++) {
		dist[i] = (int)Double.POSITIVE_INFINITY;
	}
	 
	dist[source] = 0;
	minPQ.add(sour);
	//algs 4
	while(!minPQ.isEmpty()) {
		int a = minPQ.remove().v;
		Edge edge = null;
		for(int j = 0; j<adj[a].length;j++) {
				edge = new Edge(a,adj[a][j][0],adj[a][j][1]);
				relax(edge,a);
		}	
	}
 
 }

public static void relax(Edge edge, int a){
	 int b = edge.end2(a);
	 
	 if(dist[b] > dist[a] + edge.weight) {
		 dist[b] = dist[a] + edge.weight;
		 edgeTo[b] = edge;

				minPQ.add(new vKey(b,dist[b]));	

		 }
		 
}
 
public static boolean hasPath(int a) {
	return dist[a] < (int) Double.POSITIVE_INFINITY;
}
 
public static Iterable<Edge> path(int a){
	if(!hasPath(a)) {return null;}
	
	Stack<Edge> rPath = new Stack<Edge>();
	int current = a;
	
	for(Edge e = edgeTo[a]; e!=null; e = edgeTo[current]) {
		current = e.end2(current);
		rPath.push(e);
	}
	
	return rPath;
}
 
public static void PrintPaths(int source){
	
	for(int i = 0; i < n; i++) {
		if(hasPath(i)) {
			System.out.print("The path from "+source +" to "+i+" is: ");
			
			stackPath(path(i),source);
			System.out.println(" and the total distance is: "+dist[i]);
			}
		}
}
	
	
public static void stackPath(Iterable<Edge> rPath, int source) {
	Stack<Edge> newRPath = (Stack<Edge>) rPath;
			
	System.out.print(source);
			
		while(!newRPath.isEmpty()) {
			System.out.print("-->"+newRPath.peek().end2(newRPath.peek().end1()));
			newRPath.pop();
		}
			
	
}	
 
 
 /* main()
    Contains code to test the ShortestPaths function. You may modify the
    testing code if needed, but nothing in this function will be considered
    during marking, and the testing process used for marking will not
    execute any of the code below.
 */
public static void main(String[] args) throws FileNotFoundException{
Scanner s;
if (args.length > 0){
    //If a file argument was provided on the command line, read from the file
    try{
	s = new Scanner(new File(args[0]));
    } catch(java.io.FileNotFoundException e){
	System.out.printf("Unable to open %s\n",args[0]);
	return;
    }
    System.out.printf("Reading input values from %s.\n",args[0]);
}
else{
    //Otherwise, read from standard input
    s = new Scanner(System.in);
    System.out.printf("Reading input values from stdin.\n");
}

int graphNum = 0;
double totalTimeSeconds = 0;

//Read graphs until EOF is encountered (or an error occurs)
while(true){
    graphNum++;
    if(graphNum != 1 && !s.hasNextInt())
	break;
    System.out.printf("Reading graph %d\n",graphNum);
    int n = s.nextInt();
    int[][][] adj = new int[n][][];
    
    int valuesRead = 0;
    for (int i = 0; i < n && s.hasNextInt(); i++){
	LinkedList<int[]> edgeList = new LinkedList<int[]>(); 
	for (int j = 0; j < n && s.hasNextInt(); j++){
	    int weight = s.nextInt();
	    if(weight > 0) {
		edgeList.add(new int[]{j, weight});
	    }
	    valuesRead++;
	}
	adj[i] = new int[edgeList.size()][2];
	Iterator it = edgeList.iterator();
	for(int k = 0; k < edgeList.size(); k++) {
	    adj[i][k] = (int[]) it.next();
	}
    }
    if (valuesRead < n * n){
	System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
	break;
    }
    
    // output the adjacency list representation of the graph
    for(int i = 0; i < n; i++) {
    	System.out.print(i + ": ");
    	for(int j = 0; j < adj[i].length; j++) {
    	    System.out.print("(" + adj[i][j][0] + ", " + adj[i][j][1] + ") ");
    	}
    	System.out.print("\n");
    }
    
    long startTime = System.currentTimeMillis();
    
    ShortestPaths(adj, 0);
    PrintPaths(0);
    System.out.println("DONE");
    long endTime = System.currentTimeMillis();
    totalTimeSeconds += (endTime-startTime)/1000.0;
    
    //System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
}
graphNum--;
System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
}
 
}