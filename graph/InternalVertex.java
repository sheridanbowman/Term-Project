package graph;

import list.DList;
import list.DListNode;

// Internal representation of each WUGraph vertex
public class InternalVertex {

    //Holds the real vertex object.
	protected Object realVertex;               

	//Pointer to containing dlist node (saves having to iterate through dlist for deletion).
	protected DListNode parentDlistNode;	
	
	//Holds all of the edges.
	protected DList edgeList = new DList();    
	
	//Default constructor which assigns the InternalVertex's realVertex.
	protected InternalVertex(Object vertex) {
		realVertex = vertex;

	}

}
