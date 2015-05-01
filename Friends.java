package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	boolean visited;
    
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
    	Queue<Vertex> q = new LinkedList<Vertex>();
    	
    	
    	
    	for(int v = 0; v < adjLists.length; v++){
    		if(adjLists[v].school.equalsIgnoreCase(school) && adjLists[v].visited==false){
    			q.add(adjLists[v]);
    			adjLists[v].visited = true;
    			for (Neighbor n=adjLists[v].adjList; n != null;n=n.next){
    				if (adjLists[n.vertexNum].school == null || adjLists[n.vertexNum].school != school){
    					break;
    				} else {
    					q.add(adjLists[n.vertexNum]);
    					adjLists[n.vertexNum].visited = true; 
    				}
    			}
    			while (!q.isEmpty()){
    				System.out.print(q.remove() + "--");
    			}
    		}
    	}
    	
    	for (int x =0; x <adjLists.length; x++){
    		if (adjLists[x].visited == true){
    			break;
    		} else if (x==(adjLists.length-1) &&  (adjLists[x].visited == false)){
    			System.out.println("There are no cliques for this school.");
    		}
    	}
    	
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter graph input file name: ");
        String file = sc.nextLine();
        Friends graph = new Friends(file);
        graph.print();
        System.out.println("Choose an algorithm: Shortest Chain (s), Cliques at School (cl), connectors (con), quit (q)");
        	String algo = sc.nextLine();
        	while (!algo.equalsIgnoreCase("s") && !algo.equalsIgnoreCase("cl") && !algo.equalsIgnoreCase("con") && !algo.equalsIgnoreCase("q")){
        		System.out.println("Choose an algorithm: Shortest Chain (s), Cliques at School (cl), connectors (con), quit (q)");
        		algo = sc.nextLine();
        	}
        	if (algo.equalsIgnoreCase("s")){
        		System.out.println("Enter name of first person: ");
        		String start = sc.nextLine();
        		System.out.println("Enter name of second person: ");
        		String end = sc.nextLine();
        		graph.IntroChain(start, end);
        	} else if (algo.equalsIgnoreCase("cl")){
        		System.out.println("Enter name of school: ");
        		String school = sc.nextLine();
        		graph.Clique(school);
        	} else if (algo.equalsIgnoreCase("con")){
        		graph.findCollector();
        	} else {
        		sc.close();
        		return;
        	}
        	sc.close();
    }
 
}
