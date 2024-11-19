/* Kruskal.java */

package graphalg;

import graph.*;
import set.*;

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

    WUGraph newGraph = new WUGraph();''
    // tree is where we will store result as it is computed
    PositionalList<Edge<Integer>> tree = new LinkedPositionalList<>();

    // pq entries are edges of graph, with weights as keys
    PriorityQueue<Integer, Edge<Integer>> pq = new HeapPriorityQueue<>();

    // union-find forest of components of the graph
    Partition<Vertex<V>> forest = new Partition<>();

    // map each vertex to the forest position
    Map<Vertex<V>,Position<Vertex<V>>> positions = new ProbeHashMap<>();

    for (Vertex<V> v : g.vertices())
        positions.put(v, forest.makeCluster(v));

    for (Edge<Integer> e : g.edges())
        pq.insert(e.getElement(), e);

    int size = g.numVertices();
    // while tree not spanning and unprocessed edges remain...
    while (tree.size() != size - 1 && !pq.isEmpty()) {
        Entry<Integer, Edge<Integer>> entry = pq.removeMin();
        Edge<Integer> edge = entry.getValue();
        Vertex<V>[] endpoints = g.endVertices(edge);
        Position<Vertex<V>> a = forest.find(positions.get(endpoints[0]));
        Position<Vertex<V>> b = forest.find(positions.get(endpoints[1]));
        if (a != b) {
            tree.addLast(edge);
            forest.union(a,b);
        }
    }

    return tree;
  }
}

}
