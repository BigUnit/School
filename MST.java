package codes;


import java.util.Scanner;
import java.io.File;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.*;

//from textbook and slides

class WeightedQuickU {
	private int [] id;
	private int [] size;
	private int counter;
	
	
	WeightedQuickU(int num){
		counter=num;
		size=new int[num];
		id=new int[num];
		initialize(id,num);
		initialize(size,num);
	}
	
	int [] initialize(int [] array, int len) {
		for(int i=0;i<len;i++) {
			array[i]=i;
		}
		
		return array;
	}
	
	
	int find(int a) {
		
		int root = a;
		
	while(id[root]!=root) {
		root = id[root];
	} 
	
	while(root!=a) {
		int temp = id[a];
		id[a]=root;
		a=temp;
	}
		return root;
	}
	
	void union(int a, int b) {
		int aRoot = find(a);
		int bRoot = find(b);
		
		if(bRoot != aRoot) {
			
			if(size[bRoot]<size[aRoot]) {
				id[bRoot] = aRoot;
				size[aRoot] = size[aRoot] + size[bRoot];
			} else {
				id[aRoot] = bRoot;
				size[bRoot] = size[bRoot] + size[aRoot];
			}
		
			counter--;
		
		}
	}
	
	boolean connected(int a, int b) {
		return (find(b)==find(a));
	}
	
}

class Edge implements Comparable<Edge> { //Weighted edge Java implementation from lecture slides

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
	
	


public class MST {

	 static Queue<Edge> mst = new LinkedList<Edge>();

	
	
	/* mst(adj)
    Given an adjacency matrix adj for an undirected, weighted graph, return the total weight
    of all edges in a minimum spanning tree.

    The number of vertices is adj.length
    For vertex i:
      adj[i].length is the number of edges
      adj[i][j] is an int[2] that stores the j'th edge for vertex i, where:
        the edge has endpoints i and adj[i][j][0]
        the edge weight is adj[i][j][1] and assumed to be a positive integer
 */
	
 static int mst(int[][][] adj) {
	
	 int totalWeight = 0;
	 int n = adj.length;
	 
	 
	 PriorityQueue<Edge> minPQ = new PriorityQueue<Edge>(99999*n); 
	 
	 for(int i = 0; i<n;i++) {
		 for(int j = 0; j<adj[i].length;j++) {
			 minPQ.add(new Edge(i, adj[i][j][0],adj[i][j][1]) );
	
		 }
	 }
	 
	 WeightedQuickU wqu = new WeightedQuickU(n);
	 
	 while(mst.size() < n-1 && !minPQ.isEmpty()) {
		 Edge e = minPQ.remove();
		 int a = e.end1();
		 int b = e.end2(a);
		 
		 if(!wqu.connected(a, b)) {
			 wqu.union(a, b);
			 mst.add(e);
			 totalWeight += e.weight;
		 }
	 }
	 
	 
	 
	 
	 
	 
	 
	/* Find a minimum spanning tree using Kruskal's algorithm */
	/* (You may add extra functions if necessary) */
		
	/* ... Your code here ... */
		
		
		
	/* Add the weight of each edge in the minimum spanning tree
	   to totalWeight, which will store the total weight of the tree.
	*/
	
	/* ... Your code here ... */
		
	return totalWeight;
		
 }


 public static void main(String[] args) {
	/* Code to test your implementation */
	/* You may modify this, but nothing in this function will be marked */

	int graphNum = 0;
	Scanner s;

	if (args.length > 0) {
	    //If a file argument was provided on the command line, read from the file
	    try {
		s = new Scanner(new File(args[0]));
	    }
	    catch(java.io.FileNotFoundException e) {
		System.out.printf("Unable to open %s\n",args[0]);
		return;
	    }
	    System.out.printf("Reading input values from %s.\n",args[0]);
	}
	else {
	    //Otherwise, read from standard input
	    s = new Scanner(System.in);
	    System.out.printf("Reading input values from stdin.\n");
	}
		
	//Read graphs until EOF is encountered (or an error occurs)
	while(true) {
	    graphNum++;
	    if(!s.hasNextInt()) {
		break;
	    }
	    System.out.printf("Reading graph %d\n",graphNum);
	    int n = s.nextInt();

	    int[][][] adj = new int[n][][];
	    
	    
	    
	    
	    int valuesRead = 0;
	    for (int i = 0; i < n && s.hasNextInt(); i++) {
		LinkedList<int[]> edgeList = new LinkedList<int[]>(); 
		for (int j = 0; j < n && s.hasNextInt(); j++) {
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
	    if (valuesRead < n * n) {
		System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
		break;
	    }

	    // // output the adjacency list representation of the graph
	    // for(int i = 0; i < n; i++) {
	    // 	System.out.print(i + ": ");
	    // 	for(int j = 0; j < adj[i].length; j++) {
	    // 	    System.out.print("(" + adj[i][j][0] + ", " + adj[i][j][1] + ") ");
	    // 	}
	    // 	System.out.print("\n");
	    // }

	    int totalWeight = mst(adj);
	    System.out.printf("Graph %d: Total weight of MST is %d\n",graphNum,totalWeight);

				
	}
 }

 
}