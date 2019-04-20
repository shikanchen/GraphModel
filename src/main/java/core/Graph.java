package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Shikan Chen
 * @overview this class constructs a graph container of all nodes and edges in a graph
 * and methods to manipulate these nodes and edges
 */
public class Graph<T, V> {
    private int size;
    private Map<T, Node<T, V>> nodes;
    private ArrayList<Edge<T, V>> edges;
    
    /**
     * @effects constructs an empty graph
     */
    public Graph() {
    	this.size = 0;
    	nodes = new HashMap<T, Node<T, V>>();
        edges = new ArrayList<>();
        checkRep();
    }

    /**
     * @param source - the source node that the edge coming from
     * @param destination - the sink node that the edge pointing to
     * @param label - the label for the edge
     * @effects connects the edge with two nodes, which can be the same node
     */
    public void addEdge(Node<T, V> source, Node<T, V> sink, V cost){
    	if ( source==null || sink==null ) {
			return;
		}
    	
    	Edge<T, V> edge = source.connects(sink, cost);
    	edges.add(edge);
    }
    
    /**
     * @param sourceString - the label of the source node that the edge coming from
     * @param sinkString - the label of the sink node that the edge pointing to
     * @param label - the label for the edge
     * @effects connects the edge with two nodes, which can be the same node
     */
    public void addEdge(T sourceLabel, T sinkLabel, V cost) {
    	Node<T, V> sourceNode = getNode(sourceLabel);
    	Node<T, V> sinkNode = getNode(sinkLabel);
    	addEdge(sourceNode, sinkNode, cost);
	}

    /**
     * @return the number of nodes in the graph
     */
    public int getSize() {
		return size;
	}

	/**
	 * @param label - the label for the node to be added
	 * @effects add a node labeled as `label` into the graph
	 * If there is already a node labeled the same name, do nothing 
	 */
	public void addNode(T n) {
		addNode(new Node<T, V>(n));
	}
	
	/**
	 * @param n - the node to be added
	 * @effects add node `n` into the graph
	 * If there is already a node labeled the same name as `n`, do nothing 
	 */
	public void addNode(Node<T, V> n) {
		if ( n != null && !nodes.containsKey(n.getData())) {
			nodes.put(n.getData(), n);
			size++;
		}
	}
	
    /**
     * @param label - the label of the node to be returned
     * @return the node in the graph label as `label`
     */
    public Node<T, V> getNode(T t){
        return nodes.get(t);
    }
    
    /**
     * @effects Removes all of the nodes and edges from this graph.
     * The size will be equal to 0 after this call returns.
     */
    public void clear() {
		nodes.clear();
		edges.clear();
		size = 0;
	}
    
    /**
     * @effects Removes isVisited mark on all of the nodes in this graph.
     */
    public void unvisitAll() {
    	Iterator<Entry<T, Node<T, V>>> iterator = nodes.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().unvisit();
        }
	}
    
    /**
     * @return an iterator over the sets<String, Node> in this `nodesHashMap`
     * The sets are returned in no particular order
     */
    public Iterator<Entry<T, Node<T, V>>> getNodes() {
		return nodes.entrySet().iterator();
	}
    
    /**
     * @return an iterator over the Edges in `edges` in proper sequence.
     */
    public Iterator<Edge<T,V>> getEdges() {
		return edges.iterator();
	}
    
    public Iterator<Edge<T,V>> getEdges(Node<T, V> n1, Node<T, V> n2) {
    	if (n1 == null || n2 == null) return null;
    	
		ArrayList<Edge<T, V>> connectedEdges = new ArrayList<Edge<T,V>>();
		for (Edge<T, V> edge : edges) {
			if (edge.sourceNode.equals(n1) && edge.endingNode.equals(n2)) {
				connectedEdges.add(edge);
			}
		}
    	return connectedEdges.iterator();
	}
    
    public Iterator<Edge<T,V>> getEdges(T n1, T n2) {
		return getEdges(getNode(n1), getNode(n2));
	}
    
    /**
     * @effects checks that the representation invariant holds (if any)
     * @throws RuntimeException if the rep invariant is violated
     */
	private void checkRep() throws RuntimeException {
        if (getSize() != nodes.size()) {
			throw new RuntimeException("Size is not matched");
		}
		return;
    }
	
}