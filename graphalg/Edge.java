package graphalg;

// internal instance of an edge, pointing to it's real object parents & weight
public class Edge implements Comparable<Edge> {
    protected int weight;

    // parent vertices
    protected Object internalVert1;
    protected Object internalVert2; 
    
    public Edge(Object u, Object v, int w){
        weight = w;
        internalVert1 = u;
        internalVert2 = v;
    }
    
    //Implements comparareTo to enable sorting of Edges by weight
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
