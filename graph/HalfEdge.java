package graph;

import list.DListNode;

/*
 * Each literal edge between vertices are represented by two sibling 'half edges'
 * Two 'half' edges point to each other, and are stored in either parent vertex' 
 * internal representation, and vertex hash table
 */
public class HalfEdge {
    // Pointer to sibling half-edge
    protected HalfEdge siblingEdge;

    // Parents of edges
    protected InternalVertex internalVert1;
    protected InternalVertex internalVert2; 

    // Pointer to dlist containing node for quick deletion navigation
    protected DListNode parentDListNode;

    protected int weight;

    protected HalfEdge(InternalVertex in_internalVert1, InternalVertex in_internalVert2, int in_weight){
        weight = in_weight;
        internalVert1 = in_internalVert1;
        internalVert2 = in_internalVert2; 
    }

    // Connect two siblings internal
    protected void setSiblingEdge(HalfEdge sibling) {
        siblingEdge = sibling;
    }

    // pointer to dlistnode containing this half-edge, for quick fetch/deletion
    protected DListNode getParentDListNode() {
        return parentDListNode;
    }
}
