package graph;

import list.DList;
import list.DListNode;

//Internal vertex needs to hold both the real Vertex object, and also a DList containing the edges
public class InternalVertex {

    //Holds the real vertex object
	public Object realVertex;               

	//pointer to containing dlist node (saves having to iterate through dlist for deletion)
	public DListNode parentDlistNode;	
	
	//Holds all of the edges 
	public DList edgeList = new DList();    
	
	public InternalVertex(Object vertex) {
		realVertex = vertex;
	}

}
