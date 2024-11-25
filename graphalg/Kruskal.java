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
      // theres probably a cleaner way to iterate through both lists simultaneously
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
    // --- ---- this wont work until Edge comparable is implemented
    System.out.println("edgequeue size is.."+edgeQueue.size());
    // System.out.println("front before sort is.."+edgeQueue.);

    System.out.println("front before sort is..");
    for (int i = 1; i < edgeQueue.size(); i++) {
      System.out.print(" "+((Edge)edgeQueue.nth(i)).weight);
    }

    System.out.println("");
    mergeSort(edgeQueue);

    // System.out.println("front after sort is.."+edgeQueue.front());
    System.out.println("front after sort is..");
    for (int i = 1; i < edgeQueue.size(); i++) {
      System.out.print(" "+((Edge)edgeQueue.nth(i)).weight);
    }
    System.out.println("");


    // Edge[] minEdges = new Edge[realVerts.length-1];

    // Theoretically have a sorted linkedQueue of edges w. weights, and vertexlist extracted, ready for Kruskal
    // Do the disjoint set stuff here v----v-----v----v

    DisjointSets forest = new DisjointSets(realVerts.length);

    System.out.println(" target edge count is "+ (realVerts.length-1));

    // System.out.println(Arrays.toString(minEdges));
    // while tree not spanning and unprocessed edges remain...
    while (newGraph.edgeCount != realVerts.length && !edgeQueue.isEmpty()) {
      System.out.println("  edge count in new graph is " + newGraph.edgeCount);
      try {
        Edge lowestEdge = (Edge) edgeQueue.dequeue();

        // Process the dequeued edge
        Object realVert1 = lowestEdge.internalVert1;
        Object realVert2 = lowestEdge.internalVert2;

        // get unique int reps
        int realInt1 = (int) vertexHashTable.find(realVert1).value();
        int realInt2 = (int) vertexHashTable.find(realVert2).value();
      
        System.out.println("    popped edge w. weight " + lowestEdge.weight + " between verts " +realInt1+ " and " +realInt2);

        // Edge<Integer> edge = entry.getValue();
        if (realInt1 != realInt2){
          System.out.println("       looking for "+realInt1);
          int root1 = forest.find(realInt1);
          System.out.println("       root of "+realInt1 + " is " +root1);

          System.out.println("       looking for "+realInt2);
          int root2 = forest.find(realInt2);
          System.out.println("       root of "+realInt2 + " is " +root2);

          if (root1 != root2) {
            System.out.println("   Unequal roots, edge added");

            if (forest.union(realInt1, realInt2)){
              newGraph.addEdge(realVert1, realVert2, lowestEdge.weight);
            }
            
            
          }
        }

      } catch (QueueEmptyException e) {
        System.out.println("   thrown Exception on empty");
      }
    }

    return newGraph;
  }
}
