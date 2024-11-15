package graph;

import list.DList;

public class InternalVertex {
    
	public Object realVertex;               //Holds the real vertex object
	public DList edgeList = new DList();    //Holds all of the edges 
	
	//Internal vertex needs to hold both the real Vertex object, and also a DList containing the edges
	
	public InternalVertex(Object vertex) {
		realVertex = vertex;
	}

}
