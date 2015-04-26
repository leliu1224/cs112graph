package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
 
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
    
    Vertex(String name, String school, Neighbor neighbors) {
            this.name = name;
            this.adjList = neighbors;
            this.school = school; 
            }
}
 
public class Friends {
 
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
        		adjLists[v] = new Vertex(holdLine.substring(0, lineLocation-1), holdLine.substring(lineLocation+2, holdLine.length()), null);
        		System.out.println();
        	}
        	else
        	{
        		adjLists[v] = new Vertex(holdLine.substring(0, lineLocation-1),"", null);// not students
        	}
        }
 
        // read edges
        //System.out.println(sc.next());
        //change delimiter to |
        while (sc.hasNextLine()) {
        	   sc.useDelimiter("|");//set delimiter to both | and spaces
            // read vertex names and translate to vertex numbers
            int v1 = indexForName(sc.next());
            int v2 = indexForName(sc.next());
             
            // add v2 to front of v1's adjacency list and
            // add v1 to front of v2's adjacency list
            adjLists[v1].adjList = new Neighbor(v2, adjLists[v1].adjList);
            adjLists[v2].adjList = new Neighbor(v1, adjLists[v2].adjList);
            
        }
        sc.close();
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
     
    
    /*
    public void IntroChain (String start, String end){
    	return;
    }
     */
    
    /*
    public void Clique (String school){
    	return;
    }
     */
    
    /*
    public void Connect (){
    	return;
    }
     */
    
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
        /*
        System.out.println("Choose an algorithm: Shortest Chain (s), Cliques at School (cl), connectors (con), quit (q)");
        	String algo = sc.nextLine();
        	if (algo.equalsIgnoreCase("s")){
        		System.out.println("Enter name of first person: ");
        		String start = sc.nextLine();
        		System.out.println("Enter name of second person: ");
        		String end = sc.nextLine();
        		graph.IntroChain(start, end);
        	} else if (algo.equalsIgnoreCase("cl");
        		System.out.println("Enter name of school: ");
        		String school = sc.nextLine();
        		graph.Clique(school);
        	} else if (algo.equalsIgnoreCase("con");
        		graph.Connect();
        	} else {
        		sc.close();
        		return;
        	}
    	*/
    }
 
}
