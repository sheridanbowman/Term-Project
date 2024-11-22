package graphalg;

// non-split instance of edge, pointing to it's real objects
public class Edge implements Comparable<Edge> {
    public int weight;
    public Object internalVert1;
    public Object internalVert2; 
    
    public Edge(Object u, Object v, int w){
        weight = w;
        internalVert1 = u;
        internalVert2 = v;
    }
    
    //Overrides the default compareTo method in order for the mergesort algorithm to work
    @Override
    public int compareTo(Edge otherEdge) {
        if (this.weight < otherEdge.weight) {
        	return -1;
        } 
        if (this.weight == otherEdge.weight) {
        	return 0;
        } 
        return 1;
    
    }
}
