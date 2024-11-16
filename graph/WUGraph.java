/* WUGraph.java */

/*
David First 5:
    Wugraph construct / done
    vertexCount / done
    edgeCount / done
    getVerticies / done
    (Also, the Internal Vertex Class) / might not be necessary due to Entry class, see discord groupchat message
Sheridan
    Addvertex / doneish
      Needs to add counts to maintain vert/edgeCount
    Removevertex / wip
      Needs to add counts to maintain vert/edgeCount
    Isvertex / done
    Degree / done
    Getneighbors / wip
Ethan
    addEdge
    removeEdge
    isEdge
    weight
 */


package graph;

import list.DList;
import dict.HashTableChained;
import dict.Entry;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {
   
  //Keeps track of the amount of edges. Is incremented when we add an edge, decremented when we delete one.
  public int edgeCount;


  //Vertex and Edge HashTable that hold the references to the actual vertex and the vertex pair object (for the edges)
  public HashTableChained vertexHashTable;
  public HashTableChained edgeHashTable;

  public DList internalVertices;

  public int vertexCount = 0;
  public int edgeCount = 0;

  public DList internalVertices = new DList();


  /**
   * WUGraph() constructs a graph having no vertices or edges.
   * Running time:  O(1).
   */
  public WUGraph() {

	  //Initializes the hashTables for the vertices and the edges.
	  vertexHashTable = new HashTableChained();
	  edgeHashTable = = new HashTableChained();
	  
	  //Sets edgeCount to 0.
	  edgeCount = 0;
	  internalVertices = new DList();
  }

  
  
  
  /**
   * vertexCount() returns the number of vertices in the graph.
   * Running time:  O(1).
   */

  //For both vertexCount and edgeCount, maybe we could have graph hold two int members, numEdges and numVertices thatare
  //incremented and decremented during each addition of an edge or vertex. Might be tricky to account for shared / duplicate edges.
  public int vertexCount() {
      
	  //Returns the amount of vertices by doing internalVertices.length().
	  return internalVertices.length();
	
  }
  
  
  
  
  /**
   * edgeCount() returns the total number of edges in the graph.
   * Running time:  O(1).
   */
  public int edgeCount() {  
	  
	  //Returns the edgeCount counter.
	  return this.edgeCount;
  }


  
  
  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  
  //Note to self: Beginning at the first item in the DList, we need to somehow iterate through all the internalVertices, then retrieve the actual vertex
  //We then place in an array
  public Object[] getVertices() {
	  
	  //Creates an array of type Object called result of the size of the amount of internalVertices.
	  Object[] result = new Object[this.internalVertices.length()];
	  int i = 0;
	  
	  //We begin at the first vertex, and currentVertex is null if the list is empty.
	  currentVertex = internalVertices.front();
	  
	  //While currentVertex is not null, we add the currentVertex (the internal representation) to the result array.
	  //We then assign currentVertex as the next vertex in line.
	  //The loop breaks when we reach the last node in the list.
	  while(currentVertex != null) {
		  result[i] = currentVertex;
		  i = i + 1;
		  currentVertex = internalVertices.next(currentVertex);
	  }
	  
	  //We then return the result list.
	  return result;
	 
  }
  
  


  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  //Note to self: When we add a new vertex to the graph, we need to pass it to the hashtable, and also add it to a linkedList of vertices.
  //To do so, we take the vertex Object, store it inside of the hashtable, and then we use it to create a new internalVertex item.
  //This internalVertex is then added to the current DList class member. Since internalVertex has a doubly linked list for edges, this should be good enough.
  
  public void addVertex(Object vertex) {
      //David's version
	  Object keyPlaceholder;   //Temporary placeholder for what the key should be.
	  Edge resultEntry;        //The resulting entry object that we get from inserting to the hashtable.
	  
	  //If the vertex we passed to addVertex does not exist, then we can add it to the hashtable and the internal vertices list.
	  if(!(this.isVertex(vertex))) {
		  resultEntry = vertexHashTable.insert(keyPlaceholder, vertex);
		  internalVertices.insertBack(resultEntry);
	  }
	  
    //Sheridan's version
    // Only add new verts, existing skipped
    if (!isVertex(vertex)) {
      // New internal representative
      InternalVertex newVertex = new InternalVertex(vertex);

      //into hash table
      vertexHashTable.insert(vertex, newVertex);

      //into Vert dlist
      internalVertices.insertFront(newVertex);  
    }
  }
  
  
  
  

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */

  // Delete from inside out ; Hashtable/VertexDList/internal edge list/VertexPairs
  // 
  public void removeVertex(Object vertex) {
    // only bother on graphed vertex to begin with
    Entry hashResult = vertexHashTable.find(vertex);
    if (hashResult != null) {
      InternalVertex internalVertex = (InternalVertex) hashResult.value();
      Object[] vertexNeighbors = getNeighbors((vertex)).neighborList;
      // With both vert parents on each edge, can remove each Vertex pair & its child/sibling edges now
          // WIP

    }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex) {
    return (vertexHashTable.find(vertex) != null);
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */

  // it appears that 'degree' refers to all incident edges to a vertex
  // basically, edge count
  public int degree(Object vertex) {
    Entry hashResult = vertexHashTable.find(vertex);

    // fail case on ungraphed vert being queried 
    if (hashResult == null) {
      return 0;

    } else { // Otherwise, get internal vertex, get its edge count
      InternalVertex internalVertex = (InternalVertex) hashResult.value();
      return internalVertex.edgeList.length();
    }
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex) {
    //your code here
    return new Neighbors();
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u.equals(v)) are allowed.
   *
   * Running time:  O(1).
   */
  //Could do something like, internalVertex.edgeList.insertFront......
  public void addEdge(Object u, Object v, int weight) {
    //your code here
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v) {
    //your code here
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v) {
    //your code here
    return false;

  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v) {
    //your code here
    return 0;

  }

}
