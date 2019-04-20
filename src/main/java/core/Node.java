package core;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Shikan Chen
 * @overview this class constructs a node container of it's label and all edges connecting with it
 * and methods to manipulate these edges and the label
 */
public class Node<T, V> {
	private int size;
    private LinkedList<Edge<T, V>> edges;
    private T data;
    private boolean isVisited;

    /**
     * @param label
     * @effects construct a node and initialize `edges`
     */
    public Node(T data) {
        this.data = data;
        edges = new LinkedList<Edge<T, V>>();
        unvisit();
        checkRep();
    }

	/**
     * @param destination
     * @return the edge that connects `this` and `destination`
     */
    public Edge<T, V> connects(Node<T, V> destination, V cost) {
    	Edge<T, V> edge = new Edge<T, V>(this, destination, cost);
		edges.add(edge);
		size++;
		return edge;
	}

    
	/**
	 * @return the label of this node
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param label - the label of this node to be modified
	 */
	public void setData(T data) {
		this.data = data;
	}
	
	/**
	 * @param obj The object to be compared for equality.
	 * @return true if and only if 'obj' is an instance of a Node
	 * and 'this' and 'obj' have the same label and the same data.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node<?, ?>) {
            Node<?, ?> rn = (Node<?, ?>) obj;
            
            if (this.getData().equals(rn.getData())) {
            	return true;
            }
        }
		return false;
	}

	/**
	 * @return label of this node
	 */
	@Override
	public String toString() {
		return data + "";
	}


	/**
	 * @return the number of edges coming from this node
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * @return an iterator pointing to an edge that's coming from this node
	 */
	public Iterator<Edge<T, V>> getEdges() {
		return edges.iterator();
	}
	
	/**
	 * @return if it's visited
	 */
	public boolean isVisited() {
		return isVisited;
	}

	/**
	 * @effects change isVisited to True
	 */
	public void visit() {
		this.isVisited = true;
	}
	
	/**
	 * @effects change isVisited to False
	 */
	public void unvisit() {
		this.isVisited = false;
	}
	
	/**
     * @effects checks that the representation invariant holds (if any)
     * @throws RuntimeException if the rep invariant is violated
     */
	private void checkRep() throws RuntimeException {
        if (getSize() != edges.size()) {
			throw new RuntimeException("Size is not matched");
		}
        
		return;
    }
	
}
