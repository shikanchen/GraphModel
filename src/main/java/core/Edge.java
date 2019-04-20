package core;

/**
 * @author Shikan Chen
 * @overview this class constructs a Edge container of two nodes, not necessarily distinct, connecting with this edge and its label
 * and methods to manipulate label and the two nodes
 */
public class Edge<T, V> {
	private V cost;
	Node<T, V> sourceNode, endingNode;
	
	/**
	 * @param sourseNode - the source node this edge coming from
	 * @param endingNode - the ending node this edge pointing to
	 * @param v - the label of this edge to be set
	 * @effects construct this edge connecting two nodes, which can be the same node
	 */
	public Edge(Node<T, V> sourseNode, Node<T, V> endingNode, V cost) {
		this.sourceNode = sourseNode;
		this.endingNode = endingNode;
		setCost(cost);
		checkRep();
	}

	/**
	 * @param label - the label of this edge to be set
	 */
	public void setCost(V data) {
		this.cost = data;
	}

	/**
	 * @return the label of this edge
	 */
	public V getCost() {
		return cost;
	}
	
	/**
	 * @return the endingNode of this edge
	 */
	public Node<T, V> getEndingNode() {
		return endingNode;
	}

	/**
	 * @return the sourcingNode of this edge
	 */
	public Node<T, V> getSourcingNode() {
		return sourceNode;
	}
	
	/** 
	 * @return a format of [endingNode's label]([edge's label])
	 */
	@Override
	public String toString() {
		return endingNode + "(" + cost + ")";
	}
	
	/**
     * @effects checks that the representation invariant holds (if any)
     * @throws RuntimeException if the rep invariant is violated
     */
	private void checkRep() throws RuntimeException {
//        if (sourceNode == null || endingNode == null) {
//        	throw new RuntimeException("At least one connecting node is null");
//		}
		return;
    }
}
