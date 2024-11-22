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
    public int compareTo(Edge edge1, Edge edge2) {
        if(edge1.weight < edge2.weight) {
        	return -1;
        } else if(edge1.weight == edge2.weight) {
        	return 0;
        } else if(edge1.weight > edge2.weight) {
        	return 1;
        }
    }

    //todo needs Comparable implementation on weight for linkedQueue's sorts to be able to sort it
    
}
