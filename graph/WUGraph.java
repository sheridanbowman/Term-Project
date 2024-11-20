/* WUGraph.java */

/*
David First 5:
    Wugraph construct / done
    vertexCount / done
    edgeCount / done
    getVerticies / done
    (Also, the Internal Vertex Class) / might not be necessary due to Entry class, see discord groupchat message
Sheridan
    Addvertex / done
    Removevertex / done
    Isvertex / done
    Degree / done
    Getneighbors / done
Ethan
    addEdge
    removeEdge
    isEdge
    weight
 */


package graph;

import dict.Entry;
import dict.HashTableChained;
import list.DList;
import list.DListNode;

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

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   * Running time:  O(1).
   */
  public WUGraph() {

	  //Initializes the hashTables for the vertices and the edges.
	  vertexHashTable = new HashTableChained();
	  edgeHashTable = new HashTableChained();
	  
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
	  DListNode currentVertex = internalVertices.front();
	  
	  //While currentVertex is not null, we add the currentVertex (the internal representation) to the result array.
	  //We then assign currentVertex as the next vertex in line.
	  //The loop breaks when we reach the last node in the list.
	  while(currentVertex != null) {
      InternalVertex internalVertex = (InternalVertex) currentVertex.item;
		  result[i] = internalVertex.realVertex;
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
    // Only add new verts, existing skipped
    if (!isVertex(vertex)) {
      // New internal representative
      InternalVertex newVertex = new InternalVertex(vertex);

      //into hash table
      vertexHashTable.insert(vertex, newVertex);

      //into Vert dlist
      internalVertices.insertFront(newVertex);  
      
      //pointer to dlistnode containing this internalvertex, grab pointer to what was just put in
      newVertex.parentDlistNode = internalVertices.front();
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
  // removes ref from vert hashtable, edge hashtable, and internal dlists
  public void removeVertex(Object vertex) {
    // only continue on valid vertex
    InternalVertex targetVertex = getInternalVertex(vertex);
    if (targetVertex != null) {

      // Only continue on there actually being neighbors
      Neighbors neighbors = getNeighbors(vertex);
      if (neighbors != null) {

        // Delete neighbors
        Object[] vertexNeighbors = neighbors.neighborList;

        for (Object vertexNeighbor : vertexNeighbors) {
            removeEdge(targetVertex.realVertex, vertexNeighbor);
        }
      }

      // use dlistnode pointer to remove itself from graph w.out having to iterate through dlist
      internalVertices.remove(targetVertex.parentDlistNode);

      // remove from hashtable, all done!
      vertexHashTable.remove(vertex);
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
  // basically, edge count but not double-counting self-joins
  public int degree(Object vertex) {
    // System.out.println(" in degree for "+ vertex);
    InternalVertex internalVertex = getInternalVertex(vertex);

    // break out on fail case on ungraphed vert being queried 
    if (internalVertex == null) {
      // System.out.println("  degree found nothing returning 0 ");
      return 0;

    } else { // Otherwise, get internal vertex, get its edge count
      // System.out.println("  degree found: "+internalVertex.degree);
      // return internalVertex.edgeList.length();
      return internalVertex.degree;
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

    InternalVertex refInternalVertex = getInternalVertex(vertex);
    // break out on fail case on ungraphed vert being queried 
    if (refInternalVertex == null) {
      // System.out.println("noquery breakout");
      return null;
    } 

    int internalEdgeCount = refInternalVertex.edgeList.length();
    // System.out.println("   " +internalEdgeCount+ " edges inside");
    // break out on fail case of no neighbors
    if (internalEdgeCount == 0) {
      // System.out.println("Noneighbors breakout");
      return null;
    }

    //-- Didnt break out due to errors, continue to return neighbors
    
    // New neighbor wrapper
    Neighbors neighbors = new Neighbors();

    neighbors.neighborList = new Object[internalEdgeCount];
    neighbors.weightList = new int[internalEdgeCount];

    // populate weights and neighborlist with real items, weights from edges

    DListNode currentEdgeDLNode = refInternalVertex.edgeList.front();
    for (int i = 0; i < internalEdgeCount; i++) {
        
      HalfEdge halfEdge = (HalfEdge)currentEdgeDLNode.item;

      // Add weight
      neighbors.weightList[i] = halfEdge.weight;
      // System.out.println("   edge is "+ halfEdge.internalVert1.realVertex + " " + halfEdge.internalVert2.realVertex);

      // Add one neighbor of the pair, add the one that isnt the ref internalVert
      if (halfEdge.internalVert1.equals(refInternalVertex)) {
        neighbors.neighborList[i] = halfEdge.internalVert2.realVertex;
      } else {
        neighbors.neighborList[i] = halfEdge.internalVert1.realVertex;
      }
      // System.out.println("   added neighbor "+ neighbors.neighborList[i]);
      // System.out.println("    neighborslist is "+ Arrays.toString(neighbors.neighborList));

      // increment and continue
      currentEdgeDLNode = refInternalVertex.edgeList.next(currentEdgeDLNode);
    }

    return neighbors;
    
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
  //get internal vertexs from vertex hash table, make a half edge from it 
  //null check int vertex one and two 
  public void addEdge(Object u, Object v, int weight) {
    
    // check case that verticies exist first
    // Check if verticies exist, first
    InternalVertex internalVertex_u = getInternalVertex(u);
    InternalVertex internalVertex_v = getInternalVertex(v);

    if(internalVertex_u != null && internalVertex_v != null){

      VertexPair vertexPair = new VertexPair(u, v);
      Entry vertexPairEntry = edgeHashTable.find(vertexPair);

      // check if preexisting edge exists
      if (vertexPairEntry == null) {

        HalfEdge firstHalfEdge = new HalfEdge(internalVertex_u, internalVertex_v, weight);
        HalfEdge secondHalfEdge = new HalfEdge(internalVertex_u, internalVertex_v, weight);

        firstHalfEdge.setSiblingEdge(secondHalfEdge);
        secondHalfEdge.setSiblingEdge(firstHalfEdge);

        internalVertex_u.edgeList.insertFront(firstHalfEdge);
        internalVertex_u.degree++;
        // System.out.println("  degree of "+u+" is now "+internalVertex_u.degree);

        // Only add 2nd half edge to 2nd Vertex if it's not a self-edge: otherwise duplicates
        if (u.hashCode() != v.hashCode()){
          // System.out.println("  Non-self Edge addition between "+u+" and "+v);
          internalVertex_v.edgeList.insertFront(secondHalfEdge);
          internalVertex_v.degree++;
          // System.out.println("  degree of "+v+" is now "+internalVertex_v.degree);
          
        } else {
          // System.out.println("  Self Edge addition between "+u+" and "+v);
        }
        // System.out.println("  vert " + u+" edge len is"+internalVertex_u.edgeList.length());
        // System.out.println("  vert " + v+" edge len is"+internalVertex_v.edgeList.length());
      
        edgeCount++;
        edgeHashTable.insert(vertexPair, firstHalfEdge);

      } else { 
        // edge already exists, just update it's value
        ((HalfEdge)vertexPairEntry.value()).weight = weight;
      }
    }

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
    
    // Check if verticies exist, first
    InternalVertex internalVertex_u = getInternalVertex(u);
    InternalVertex internalVertex_v = getInternalVertex(v);

    // Also, only remove edge if it exists
    if(internalVertex_u != null && internalVertex_v != null && isEdge(u, v)){

      internalVertex_u.degree--;
      // System.out.println("  degree of "+u+" is now "+internalVertex_u.degree);

      // Only deduct degree again if it's not a self edge, when u & v are not the same instance
      if (u.hashCode() != v.hashCode()){
        internalVertex_v.degree--;    
        // System.out.println("  degree of "+v+" is now "+internalVertex_v.degree);    
      }

      edgeCount--;
      VertexPair edge = new VertexPair(u, v);

      edgeHashTable.remove(edge);
      internalVertex_u.edgeList.remove(internalVertex_u.parentDlistNode);
      internalVertex_v.edgeList.remove(internalVertex_v.parentDlistNode);
    }
  }

  // wrapper to either null or the internal vertex, if found within the vertexHashTable
  private InternalVertex getInternalVertex(Object u){
    Entry hashResult_u = vertexHashTable.find(u);
    if (hashResult_u != null) {
      return (InternalVertex) hashResult_u.value();
    } else {
      return null;
    }
  }

  // private VertexPair getVertexPair(Object u, Object v){

  // }
  // // returns either null or the internal vertex, if found
  // private HalfEdge getHalfEdge() {

  // }
  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v){
    VertexPair edge = new VertexPair(u, v);
    if(edgeHashTable.find(edge) == null) {
      return false;
    }
    return true;
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
    VertexPair edge = new VertexPair(u, v);
    Entry hashResult = edgeHashTable.find(edge);
    if(hashResult == null){
      return 0;
    } else {
      return ((HalfEdge) hashResult.value()).weight;
    }
  }

}
