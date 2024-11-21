package graph;

import list.DListNode;

public class HalfEdge {
    public HalfEdge siblingEdge;
    public int weight;
    public InternalVertex internalVert1;
    public InternalVertex internalVert2; 
    public DListNode parentDListNode;

    public HalfEdge(InternalVertex in_internalVert1, InternalVertex in_internalVert2, int in_weight){
        weight = in_weight;
        internalVert1 = in_internalVert1;
        internalVert2 = in_internalVert2; 
    }

    public void setSiblingEdge(HalfEdge sibling) {
        siblingEdge = sibling;
        sibling.siblingEdge = siblingEdge;
    }

    public DListNode getParentDListNode()
    {
        return parentDListNode;
    }
}
