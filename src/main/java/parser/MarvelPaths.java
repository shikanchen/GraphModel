package parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import core.Edge;
import core.Graph;
import core.GraphWrapper;
import core.Node;

public class MarvelPaths {
	private GraphWrapper graphWrapper;
	private Graph<String, String> graph;
	
	public MarvelPaths() {
		graph = new Graph<String, String>();
		graphWrapper = new GraphWrapper(graph);
	}
	
	private void initGraph(Map<String,Set<String>> charsInBooks, Set<String> chars) {
		// add nodes
		for (String c : chars) {
			graph.addNode(c);
		}
		
		// add edges
		for (String bookString : charsInBooks.keySet()) {
			Set<String> charsInBookString = charsInBooks.get(bookString);
			
			for (String char1 : charsInBookString) {
				for (String char2 : charsInBookString) {
					if (char1 != char2) {
						graph.addEdge(char1, char2, bookString);
					}
					
				}
			}
		}
		return;
	}
	
	private LinkedList<Edge<String, String>> getShortestPath(Node<String, String> startingNode, Node<String, String> destinationNode) {
		if (startingNode.equals(destinationNode)) return new LinkedList<Edge<String, String>>();

		// Initialize queue and map for storing data
		LinkedList<Node<String, String>> toVisitQueue = new LinkedList<Node<String, String>>();
		Map<Node<String, String>, LinkedList<Edge<String, String>>> paths = new HashMap<Node<String, String>, LinkedList<Edge<String, String>>>();
		
		// distance from startingNode should be 0
		paths.put(startingNode, new LinkedList<Edge<String, String>>());
		toVisitQueue.add(startingNode);
		startingNode.visit();

		// loop until the queue is empty, in another word all nodes connected has been checked
		while (!toVisitQueue.isEmpty()) {
			Node<String, String> nextNode = toVisitQueue.poll();
			LinkedList<Edge<String, String>> path = new LinkedList<Edge<String, String>>(paths.get(nextNode));
			
			// check all surrounding Edges
			Iterator<Edge<String, String>> surroundingIterator = graphWrapper.listChildren(nextNode);
			while (surroundingIterator.hasNext()) {
				Edge<String, String> surroundingEdge = surroundingIterator.next();
				Node<String, String> surroundingNode = surroundingEdge.getEndingNode();
				if (!surroundingNode.isVisited()) {
					toVisitQueue.add(surroundingNode);
					LinkedList<Edge<String, String>> path_dash = new LinkedList<Edge<String, String>>(path);
					path_dash.add(surroundingEdge);
					paths.put(surroundingNode, path_dash);
					surroundingNode.visit();
				}
			}
		}
		
		// clear marks
		graph.unvisitAll();
		return paths.get(destinationNode);

	}
	
	public void createNewGraph(String filename) {
		Map<String, Set<String>> charsInBooks = new HashMap<String,Set<String>>();
		Set<String> chars = new HashSet<String>();
		
		// parse file into map data structure
		try {
			MarvelParser.readData(filename,charsInBooks,chars);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// set up graph
		initGraph(charsInBooks, chars);
	}
	
	public String findPath(String node1, String node2) {
		boolean b1 = graph.getNode(node1) == null;
		boolean b2 = graph.getNode(node2) == null;
		
		// unknown output select
		String output = "";
		if (b1 && b2 && node1.equals(node2)) {
			output += "unknown character " + node1 + "\n";
		} else {
			if (b1) {
				output += "unknown character " + node1 + "\n";
			}
			if (b2) {
				output += "unknown character " + node2 + "\n";
			}
		}
		
		// find path select
		if (!b1 && !b2) {
			LinkedList<Edge<String, String>> path = getShortestPath(graph.getNode(node1), graph.getNode(node2));
			output = "path from " + node1 + " to " + node2 + ":\n";
			if (path == null) {
				output += "no path found\n";
			} else{
				for (Edge<String, String> edge : path) {
					output += edge.getSourcingNode() + " to " + edge.getEndingNode() + " via " + edge.getCost() + "\n";
				}
			}
		}
		
		return output;
	}
}