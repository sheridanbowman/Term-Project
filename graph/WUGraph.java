/* WUGraph.java */

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
  private int edgeCount;


  //Vertex and Edge HashTable that hold the references to the actual vertex and the vertex pair object (for the edges).
  private HashTableChained vertexHashTable;
  private HashTableChained edgeHashTable;

  private DList internalVertices;

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
   * This is done by returning the length class member of the internalVertices DList created 
   * to hold all the vertices. Returns an int representing the number of vertices.
   */
  
  public int vertexCount() {
    //Returns the amount of vertices by doing internalVertices.length().
    return internalVertices.length();
  }
  
  
  
  
  /**
   * edgeCount() returns the total number of edges in the graph.
   * Running time:  O(1).
   * This is done by returning the edgeCount class member of the WUGraph object the user has created. 
   * edgeCount is incremented every time a new edge is added, and avoids counting duplicates.
   * Returns an int representing the number of edges.
   */
  
  public int edgeCount() {  
    //Returns the edgeCount counter.
    return this.edgeCount;
  }


  
  
  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices. If the graph has no vertices, the array has length
   * zero. This array is of type object. How the method works is it begins by
   * creating an Object array called result, which is initialized to the
   * size of the number of vertices in the graph. Then, beginning at the first
   * vertex, we set currentVertex to the front vertex of the doubly linked vertex
   * list. A while loop iterates for every vertex in the internalVertices list. 
   * In each iteration, internalVertex is assigned to currentVertex's item, which is
   * the internal vertex. We add internalVertex's real vertex that corresponds to
   * the actual graph to the result array at index i. We then increment i and 
   * assign currentVertex to be the next vertex. This eventually adds every
   * vertex into the result Object array.
   *
   *
   * Running time:  O(|V|).
   */
  
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
   * If this object is already a vertex of the graph, the graph is unchanged.
   * The vertex is added to the vertexHashTable, with the real vertex as the hash key,
   * and initialzed for internal representation.
   *
   * Running time:  O(1).
   */
  
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
   * removeVertex() removes a vertex from the graph. All edges incident on the
   * deleted vertex are removed. If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged. Takes in a vertex as
   * an argument. The method begins by setting the InternalVertex targetVertex to be
   * the internalVertex that matches the argument vertex, using the getInternalVertex
   * method. If the targetVertex is not null, we set neighbors to be all the neighbors
   * of the argument vertex. If neighbors is not null, we create an Object array called
   * vertexNeighbors and set it to be neighbor's neighborList. For every vertexNeighbor
   * in vertexNeighors, we call removeEdge to remove the edge between the realVertex in
   * targetVertex and vertexNeighbor. We then use the DListNode pointer to remove itself from
   * the graph, without having to iterate through the DList. We then remove the vertex
   * from the hashTable. 
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */

  public void removeVertex(Object vertex) {
    // only continue on valid vertex
    InternalVertex targetVertex = getInternalVertex(vertex);
    if (targetVertex != null) {

      // Only continue when there's neighbors to remove
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
   * the graph. Takes in a vertex as the argument, and returns either true
   * or false if vertexHashTable.find(vertex) returns true or false.
   *
   * Running time:  O(1).
   */
  
  public boolean isVertex(Object vertex) {
    return (vertexHashTable.find(vertex) != null);
  }

  
  
  
  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned. The method begins by taking in a vertex
   * as the argument. It then assigns internalVertex with the internalVertex
   * associated with vertex, using the getInternalVertex method. If the 
   * internalVertex is null, we return 0, meaning that there is a degree of 0.
   * Else, we return the degree of the internalVertex. This method returns an int
   * and doesn't count self edges.
   *
   * Running time:  O(1).
   */

  public int degree(Object vertex) {
    //Sets internalVertex to the InternalVertex object held inside of vertex.
    InternalVertex internalVertex = getInternalVertex(vertex);

    // Break out on fail case on ungraphed vert being queried 
    if (internalVertex == null) {
      return 0;
        
    } else { // Otherwise, get internal vertex, get its edge count
      return internalVertex.edgeList.length();
        
    }
  }

  
  
  
  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge. The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object). The method
   * takes in a vertex object and finds the neighbors surrounding it.
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  
  public Neighbors getNeighbors(Object vertex) {

    //Assigns refInternalVertex with the internalVertex associated with vertex.
    InternalVertex refInternalVertex = getInternalVertex(vertex);

    //Breaks on the fail case of an ungraphed vertex being passed through the method.
    if (refInternalVertex == null) {
      return null;
    } 
    
    //Assigns internalEdgeCount with the length of the referenced internal vertex's edgeList.
    int internalEdgeCount = refInternalVertex.edgeList.length();
    
    //Checks for and breaks out on the fail case of there being no neighbors.
    if (internalEdgeCount == 0) { 
      return null;
    }

    //-- Didnt break out due to errors, continue to return neighbors.
    // New neighbor wrapper
    Neighbors neighbors = new Neighbors();

    neighbors.neighborList = new Object[internalEdgeCount];
    neighbors.weightList = new int[internalEdgeCount];

    //Populate weights and neighborlist with real items, weights from edges.
    DListNode currentEdgeDLNode = refInternalVertex.edgeList.front();
    for (int i = 0; i < internalEdgeCount; i++) {
    
      HalfEdge halfEdge = (HalfEdge)currentEdgeDLNode.item;

      // Add weight.
      neighbors.weightList[i] = halfEdge.weight;

      // Add one neighbor of the pair, add the one that isnt the ref internalVert.
      if (halfEdge.internalVert1.equals(refInternalVertex)) {
        neighbors.neighborList[i] = halfEdge.internalVert2.realVertex;
      } else {
        neighbors.neighborList[i] = halfEdge.internalVert1.realVertex;
      }
        
      // Move on to next edge node
      currentEdgeDLNode = refInternalVertex.edgeList.next(currentEdgeDLNode);
    }  
    
    return neighbors;
  }

  
  
  
  /**
   * addEdge() adds an edge (u, v) to the graph. Takes in two objects, u and v,
   * alongside a weight as the arguments. If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u.equals(v)) are allowed. In short, it gets the internal vertices 
   * from the vertex hash table, makes half edges from it, then null checks
   * the two vertices.
   *
   * Running time:  O(1).
   */
  
  public void addEdge(Object u, Object v, int weight) {
    
    // Check if verticies exist, first
    InternalVertex internalVertex_u = getInternalVertex(u);
    InternalVertex internalVertex_v = getInternalVertex(v);

    if(internalVertex_u != null && internalVertex_v != null){
      VertexPair vertexPair = new VertexPair(u, v);
      Entry vertexPairEntry = edgeHashTable.find(vertexPair);

      //Checks if preexisting edge exists
      if (vertexPairEntry == null) { 	  
        //We initialize firstHalfEdge and secondHalfEdge as two HalfEdges, with
        //the same weight yet and same vertices u and v.
        HalfEdge firstHalfEdge = new HalfEdge(internalVertex_u, internalVertex_v, weight);
        HalfEdge secondHalfEdge = new HalfEdge(internalVertex_u, internalVertex_v, weight);
        
        //We set the two half edges to be siblings through the setSiblingEdge() method.
        firstHalfEdge.setSiblingEdge(secondHalfEdge);
        secondHalfEdge.setSiblingEdge(firstHalfEdge);
        
        //We then insert both edges into the edgeList corresponding to either u or v.
        //the parentDListNode of the halfEdge to be the first item in the edgeList.
        internalVertex_u.edgeList.insertFront(firstHalfEdge);
        firstHalfEdge.parentDListNode = internalVertex_u.edgeList.front();

        // Only add 2nd half edge to 2nd Vertex if it's not a self-edge: otherwise duplicates.
        if (u.hashCode() != v.hashCode()){
          internalVertex_v.edgeList.insertFront(secondHalfEdge);
          secondHalfEdge.parentDListNode = internalVertex_v.edgeList.front();
    
        }
        
        //We then increment the edgeCount class member and insert the vertexPair alongside the firstHalfEdge.
        edgeCount++;
        edgeHashTable.insert(vertexPair, firstHalfEdge);

      } else { 
        // In this case, the edge already exists, so just update it's values.
        HalfEdge halfEdge= (HalfEdge)vertexPairEntry.value();
        halfEdge.weight = weight;
        halfEdge.siblingEdge.weight = weight;
      }
    }
  }

  
  
  
  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged. The method takes in two objects, u and v, then calls the internal
   * vertices associated with them and remove it only if it exists. 
   *
   * Running time:  O(1). 
   */
  
  public void removeEdge(Object u, Object v) {
    // Check if verticies exist, first
    InternalVertex internalVertex_u = getInternalVertex(u);
    InternalVertex internalVertex_v = getInternalVertex(v);
    VertexPair vertexPair = new VertexPair(u, v);

    //Removes edge only if it exists. Checks the two internal vertices for null and calls isEdge on both.
    if(internalVertex_u != null && internalVertex_v != null && isEdge(u, v)){
        
      HalfEdge firstHalfEdge = ((HalfEdge)edgeHashTable.find(vertexPair).value());

      internalVertex_u.edgeList.remove(firstHalfEdge.getParentDListNode());
      getNeighbors(u);

      // Only add 2nd half-edge to 2nd vert when u & v are not the same instance (a self-reference).
      if (u.hashCode() != v.hashCode()){
        HalfEdge secondHalfEdge = firstHalfEdge.siblingEdge;

        internalVertex_v.edgeList.remove(secondHalfEdge.getParentDListNode());
        getNeighbors(v);
      }

      edgeCount--;
      edgeHashTable.remove(vertexPair);
    } 
  }

  
  
  
  /** 
   * getInternalVertex() is a wrapper function to return the unpacked result, or 
   * null value of an Object u queried against the internal hash table of vertices. 
   * Returns the internalVertex internal representation of the object u, or null if
   * it doesn't exist 
   */

  private InternalVertex getInternalVertex(Object u){
    //Assign hashResult_u with the result of calling find on vertexHashTable with u as the argument.
    Entry hashResult_u = vertexHashTable.find(u);
    
    //If the hashResult_u is not null, we return the InternalVertex object associated with the hashResult.
    //Else, returns null.
    if (hashResult_u != null) {
      return (InternalVertex) hashResult_u.value();
    } else {
      return null;
    }
  }


  
  
  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph). u and v
   * are the parameters that represent two vertex objects. Returns a boolean
   * of either true or false depending on whether or not the two objects consist
   * an edge.
   *
   * Running time:  O(1).
   */

  //Build a key out of real vertices u & v for checking Edge hash table; then check it
  public boolean isEdge(Object u, Object v){
    VertexPair edge = new VertexPair(u, v);
    return edgeHashTable.find(edge) != null;
  }

  
  
  
  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph). Takes in two objects u and v as the
   * parameters, and returns an int which represents the weight of the edge between
   * them.
   *
   * Running time:  O(1).
   */
  
  public int weight(Object u, Object v) {
    // create new key to search Edge hash table with
    VertexPair edge = new VertexPair(u, v);
    Entry hashResult = edgeHashTable.find(edge);
    
    // If there's a match, unpack hashResult's internal halfEdge and return it's weight
    if(hashResult == null){
      return 0;
    } else {
      return ((HalfEdge) hashResult.value()).weight;
    }
  }
}
