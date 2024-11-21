package graphalg;

// non-split instance of edge, pointing to it's real objects
public class Edge {
    public int weight;
    public Object internalVert1;
    public Object internalVert2; 
    
    public Edge(Object u, Object v, int w){
        weight = w;
        internalVert1 = u;
        internalVert2 = v;
    }

    //todo needs Comparable implementation on weight for linkedQueue's sorts to be able to sort it
    
}
