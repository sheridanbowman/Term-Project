/* Kruskal.java */

package graphalg;

import dict.HashTableChained;
import graph.*;
import list.LinkedQueue;
import list.QueueEmptyException;
import static list.sorts.mergeSort;
import set.DisjointSets;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */

  public static WUGraph minSpanTree(WUGraph g) {

    // New graph to fill with MST & return
    WUGraph newGraph = new WUGraph();
    
    // real verticies from old graph
    Object[] realVerts = g.getVertices();

    //hashtable to map back and forth from realVerts list : uniqueInt <-> vert
    HashTableChained vertexHashTable = new HashTableChained();

    // unsorted edges init
    LinkedQueue edgeQueue = new LinkedQueue(); 
    
    // for each vertex, check its neighbors, and add those edges to queue
    for (int i = 0; i < realVerts.length; i++) {
        
      Object realVert = realVerts[i];

      // transfer to vert to result graph, no matter what vertex count will be same
      newGraph.addVertex(realVert);  

      // add to hashtable too, keep up to date for preventing redundant adds in next inner loop
      vertexHashTable.insert(realVert, i);
      

      // for each vertex's neighbor, add edge from parent vert + neighbor, weight
      Neighbors vertNeighborWrapper = g.getNeighbors(realVert);

      int[] vertNeighborWeights = vertNeighborWrapper.weightList;
      Object[] vertNeighborList = vertNeighborWrapper.neighborList;

      for (int j = 0; j < vertNeighborWeights.length; j++) {
          
        Object vertNeighbor = vertNeighborList[j];

        // Check against existing hashtable of verts and dont add if it exists
        // if a vertexNeighbor was already added to newGraph, we don't need to add it again; its been thoroughly added 
        if (vertexHashTable.find(vertNeighbor) != null) {
          
          Edge newEdge = new Edge(realVert, vertNeighbor, vertNeighborWeights[j]);
          edgeQueue.enqueue(newEdge);
        }     
      }
    }

    //sort the edgequeue by weight
    mergeSort(edgeQueue);


    DisjointSets forest = new DisjointSets(realVerts.length);

    // while tree not spanning and unprocessed edges remain; keep iterating
    while (newGraph.edgeCount() != realVerts.length-1 && !edgeQueue.isEmpty()) {
       
      try {
        Edge lowestEdge = (Edge) edgeQueue.dequeue();

        // get parent verts of lowest edge
        Object realVert1 = lowestEdge.internalVert1;
        Object realVert2 = lowestEdge.internalVert2;
 
        // get unique int representations
        int realInt1 = (int) vertexHashTable.find(realVert1).value();
        int realInt2 = (int) vertexHashTable.find(realVert2).value(); 
       
        // never add a self-edge, in MST it's always redundant
        if (realInt1 != realInt2){ 

          // get two verticies int roots
          int root1 = forest.find(realInt1);
          int root2 = forest.find(realInt2); 

          // roots are unique, combine sets and add edge
          if (root1 != root2) {
            forest.union(root1, root2);
            newGraph.addEdge(realVert1, realVert2, lowestEdge.weight);
            
          }
        } 
 
      } catch (QueueEmptyException e) {
        break;
      }

    }

    return newGraph;
  }
}
