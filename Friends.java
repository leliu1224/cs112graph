package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
 
class Neighbor {
    public int vertexNum;
    public Neighbor next;
    public Neighbor(int vnum, Neighbor nbr) {
            this.vertexNum = vnum;
            next = nbr;
    }
}
 
class Vertex {
    String name;
    String school;//add school variable for students
    Neighbor adjList;
    int dist;
	Vertex path;
	boolean marked;
	int dfsnum, back;
	boolean printMark;
    
    Vertex(String name, String school, Neighbor neighbors) {
            this.name = name;
            this.adjList = neighbors;
            this.school = school;
            boolean marked = false;
            }
}
 

public class Friends {
	
    int dfsnumber = 1;
    int backnumber = 1;
    Vertex[] adjLists;
     
    public Friends(String file) throws FileNotFoundException {
         
        Scanner sc = new Scanner(new File(file));
                  
        adjLists = new Vertex[sc.nextInt()];//instantiate the array
        sc.nextLine();
        // read vertices
        for (int v=0; v < adjLists.length; v++) {
        	String holdLine =  sc.nextLine(); //Hold the entire line
        	int lineLocation = holdLine.indexOf('|'); //index of the | in the line
        	if(holdLine.charAt(lineLocation+1) == 'y'){
        		adjLists[v] = new Vertex(holdLine.substring(0, lineLocation), holdLine.substring(lineLocation+3, holdLine.length()), null);
        	}
        	else
        	{
        		adjLists[v] = new Vertex(holdLine.substring(0, lineLocation),"", null);
        	}
        }
 
        // read edges
        //System.out.println(sc.next());
        //change delimiter to |
        while (sc.hasNextLine()) {
        	sc.useDelimiter("[|\n]");
            // read vertex names and translate to vertex numbers
            int v1 = indexForName(sc.next());
            int v2 = indexForName(sc.next());
             
            // add v2 to front of v1's adjacency list and
            // add v1 to front of v2's adjacency list
            adjLists[v1].adjList = new Neighbor(v2, adjLists[v1].adjList);
            adjLists[v2].adjList = new Neighbor(v1, adjLists[v2].adjList);
            
        }
    }
     
    int indexForName(String name) {
        for (int v=0; v < adjLists.length; v++) {
            if (adjLists[v].name.equals(name)) {
                return v;
            }
        }
        return -1;
    }   
     
    public void print() {
        System.out.println();
        for (int v=0; v < adjLists.length; v++) {
            System.out.print(adjLists[v].name);
            for (Neighbor nbr=adjLists[v].adjList; nbr != null;nbr=nbr.next) {
                System.out.print(" --> " + adjLists[nbr.vertexNum].name);
            }
            System.out.println("\n");
        }
    }
    public void IntroChain (String start, String end){
    	Vertex s = null;
    	Queue<Vertex> q = new LinkedList<Vertex>();
    	for(int v = 0; v < adjLists.length; v++){
    		if(adjLists[v].name.equals(start)){
    			s = adjLists[v];//set the starting point
    		}
    		adjLists[v].dist =  (int) Math.pow(adjLists.length, 3) + 1;//set distance to a high number
    	}
    	s.dist = 0; //set the starting distance
    	q.add(s);
    	while(!q.isEmpty()){
    		Vertex v = q.remove();
            for (Neighbor w=v.adjList; w != null;w=w.next) {
                System.out.println(" --> " + adjLists[w.vertexNum].name);
                if(adjLists[w.vertexNum].dist >= Math.pow(adjLists.length, 3)+1){
                	adjLists[w.vertexNum].dist = v.dist+1;
                	adjLists[w.vertexNum].path = v;
                	q.add(adjLists[w.vertexNum]);
                }
            }
    	}
    	for(int v = 0; v < adjLists.length; v++){
    		
    		if(adjLists[v].name.equals(end)){
    			Stack<String> pathArray = new Stack<String>();
    			pathArray.push(adjLists[v].name);
    			while(adjLists[v].path != null){
    				pathArray.push(adjLists[v].path.name);
    				adjLists[v].path = adjLists[v].path.path;
    			}
    			if(pathArray.size() == 1){
    				System.out.println("There is no way to reach " + end + " from " + start);
    			}
    			else{
    				System.out.println("The shortest path from " + start + " to " + end +" is:");
    				System.out.print(pathArray.pop());
    				while(!pathArray.isEmpty()){ 					
    					System.out.print(" --> " + pathArray.pop());
    				}
    			}
    			break;
    		}
    	}  	
    	return;
    }
    
    public void findCollector() {
        boolean[] collectorArray = new boolean[adjLists.length];
        for(int v=0; v < adjLists.length; v++){
        	if(!adjLists[v].marked){
        		dfs(adjLists, v, collectorArray);
        	}
        }
        for(int i = 0; i<collectorArray.length; i++){
        	if(collectorArray[i] == true)
        	System.out.print(adjLists[i].name + " ,");
        }
    }

    // depth first search from v
    private void dfs(Vertex[] G, int v, boolean[] collectors) {
    	
    	System.out.println("Processing " + adjLists[v].name);
        adjLists[v].marked = true;
    	adjLists[v].dfsnum = dfsnumber;
    	dfsnumber++;
    	adjLists[v].back = backnumber;
    	backnumber++;
        for (Neighbor w=adjLists[v].adjList; w != null;w=w.next) {
        	
            if (!adjLists[w.vertexNum].marked) {
                dfs(G, w.vertexNum, collectors);
                if(adjLists[v].dfsnum > adjLists[w.vertexNum].back){
                	adjLists[v].back = Math.min(adjLists[v].back, adjLists[w.vertexNum].back );
                }
                if(isConnector(adjLists[v], adjLists[w.vertexNum])){
                	collectors[v] = true;
                	System.out.println(adjLists[v].name);
                }
                
            }
            else{
            	adjLists[v].back = Math.min(adjLists[v].back, adjLists[w.vertexNum].dfsnum );
            }
        }
    }
    
    private boolean isConnector(Vertex v, Vertex w){
    	if(v.dfsnum <= w.back && v.dfsnum != 1){
    		return true;
    	}
//    	if(v.dfsnum == 1 && v.adjList.next == null){
//    		return true;
//    	}
    	else{
    		return false;
    	}
    }

    public void Clique (String school){
    	
    	for(int v=0; v < adjLists.length; v++){
    		if(!adjLists[v].school.equals(school)){
    			adjLists[v] = null;
    		}
    	}
    	
    	Vertex[] clique = new Vertex[adjLists.length];
    	ArrayList<Vertex[]> group = new ArrayList<Vertex[]>();
        for(int v=0; v < adjLists.length; v++){
        	if(adjLists[v] != null && !adjLists[v].marked){
        		dfsClique(adjLists, v, clique);
        		System.out.println("Clique");
        		cliquePrint(clique);	
        	}
        }
        
    }
        
        private void dfsClique(Vertex[] G, int v, Vertex[] clique) {
        	//System.out.println("Processing " + adjLists[v].name);
        	G[v].marked = true;
        	clique[v] = G[v];
            for (Neighbor w=G[v].adjList; w != null;w=w.next) {
            	if(G[w.vertexNum] != null){
	            	//System.out.println("testing " + G[w.vertexNum].name);
	                if (!G[w.vertexNum].marked) {
	                    dfsClique(G, w.vertexNum,clique);            
	                }
            	}
            }
        }
        
        private void cliquePrint(Vertex[] clique) {
            System.out.println();
            for (int v=0; v < clique.length; v++) {
            	if(clique[v] != null && !clique[v].printMark){
	                System.out.print(clique[v].name);
	                clique[v].printMark = true;
		                for (Neighbor nbr=clique[v].adjList; nbr != null;nbr=nbr.next) {
		                	if(clique[nbr.vertexNum] != null){
		                		System.out.print(" -- " + clique[nbr.vertexNum].name);
		                		clique[nbr.vertexNum].printMark = true;
		                	}
		                }
	                
	                System.out.println("\n");
            
            	}
            }
        }
    	 


	/**
     * @param args
     */
    public static void main(String[] args) 
    throws IOException {
        // TODO Auto-generated method stub
        Scanner sc = new Scanner(System.in);
        //System.out.print("Enter graph input file name: ");
        //String file = sc.nextLine();
        String file = "test1";
        Friends graph = new Friends(file);
        graph.print();
        graph.Clique("rutgers");
        //graph.IntroChain("heather", "samir");
        //graph.findCollector();
    }
 
}
