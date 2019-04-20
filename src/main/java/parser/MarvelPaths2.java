package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import core.Edge;
import core.Graph;
import core.Node;

public class MarvelPaths2 {
	private Graph<String, Double> graph;
	
	public MarvelPaths2() {
		graph = new Graph<String, Double>();
	}
	
	private void initGraph(Map<String,Set<String>> charsInBooks, Set<String> chars) {
		graph.clear();
		
		// add nodes
		for (String c : chars) {
			graph.addNode(c);
		}
		
		// add edges
		
		Map<Node<String, Double>, Map<Node<String, Double>, Integer>> edgeMap = new HashMap<Node<String,Double>, Map<Node<String,Double>,Integer>>();
		
		for (String bookString : charsInBooks.keySet()) {
			Set<String> charsInBookString = charsInBooks.get(bookString);
			
			for (String char1 : charsInBookString) {
				for (String char2 : charsInBookString) {
					if (char1 != char2) {
						if (edgeMap.containsKey(graph.getNode(char1))) {
							if (edgeMap.get(graph.getNode(char1)).containsKey(graph.getNode(char2))) {
								int newCostDeno = edgeMap.get(graph.getNode(char1)).get(graph.getNode(char2));
								edgeMap.get(graph.getNode(char1)).put(graph.getNode(char2), newCostDeno+1);
							} else {
								edgeMap.get(graph.getNode(char1)).put(graph.getNode(char2), 1);
							}
						} else {
							edgeMap.put(graph.getNode(char1), new HashMap<Node<String,Double>, Integer>());
							edgeMap.get(graph.getNode(char1)).put(graph.getNode(char2), 1);
						}
					}
				}
			}
		}
		
		Iterator<Node<String, Double>> firstNodeIterator = edgeMap.keySet().iterator();
		while (firstNodeIterator.hasNext()) {
			Node<String, Double> firstNode = firstNodeIterator.next();
			Iterator<Node<String, Double>> secondNodeIterator = edgeMap.get(firstNode).keySet().iterator();
			while (secondNodeIterator.hasNext()) {
				Node<String, Double> secondNode = secondNodeIterator.next();
				int costDeno = edgeMap.get(firstNode).get(secondNode);
				graph.addEdge(firstNode, secondNode, 1.0/costDeno);
			}
			
		}
		
		
	}
	
	private LinkedList<Edge<String, Double>> getShortestPath(Node<String, Double> startingNode, Node<String, Double> destinationNode) {
		
		PriorityQueue<LinkedList<Edge<String, Double>>> activePaths = new PriorityQueue<LinkedList<Edge<String,Double>>>(5, new CostComparator());
		ArrayList<Node<String, Double>> fininshedNodes = new ArrayList<Node<String,Double>>();

	    // Initially we only know of the path from start to itself, which has a cost
	    // of zero because it contains no edges.
		Iterator<Edge<String, Double>> initEdgesIterator = startingNode.getEdges();
		
		while (initEdgesIterator.hasNext()) {
			LinkedList<Edge<String, Double>> initPath = new LinkedList<Edge<String,Double>>();
			initPath.add(initEdgesIterator.next());
			activePaths.add(initPath);
		}
	    while (!activePaths.isEmpty()) {
	        
	    	// minPath is the lowest­cost path in active and is the minimum­cost
	        // path for some node
	        LinkedList<Edge<String, Double>> minPath = activePaths.poll();
	        Node<String, Double> minDest = minPath.peekLast().getEndingNode();
	        if (minDest.equals(destinationNode)) {
	        	graph.unvisitAll();
	        	return minPath;
	        }
	        if (fininshedNodes.contains(minDest)) {
	        	continue;
	        }
	        
	        Iterator<Edge<String, Double>> nextEdgeIterator = minDest.getEdges();
	        while (nextEdgeIterator.hasNext()) {
				Edge<String, Double> nextEdge = nextEdgeIterator.next();
				Node<String, Double> nextNode = nextEdge.getEndingNode();
				
				if (!fininshedNodes.contains(nextNode) && !minDest.isVisited()) {
					
					// If we don't know the minimum­cost path from start to child, examine the path we've just found
					LinkedList<Edge<String, Double>> newPath = new LinkedList<Edge<String,Double>>(minPath);
					newPath.add(nextEdge);
					activePaths.add(newPath);
				}
				
			}
			fininshedNodes.add(minDest);
			minDest.visit();
	    }
	    
	    graph.unvisitAll();
		return null;

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
			double totalCost = 0;
			boolean isFound = true;
			if (node1.equals(node2)) {
				output = "path from " + node1 + " to " + node2 + ":\n";
			} else {
				LinkedList<Edge<String, Double>> path = getShortestPath(graph.getNode(node1), graph.getNode(node2));
				output = "path from " + node1 + " to " + node2 + ":\n";
				if (path == null) {
					output += "no path found\n";
					isFound = false;
				} else{
					for (Edge<String, Double> edge : path) {
						output += edge.getSourcingNode() + " to " + edge.getEndingNode() + String.format(" with weight %.3f\n", edge.getCost());
						totalCost += edge.getCost();
					}
				}
			}
			output += isFound?String.format("total cost: %.3f\n", totalCost):"";
		}
		
		return output;
	}
	
	class CostComparator implements Comparator<LinkedList<Edge<String, Double>>> {

		@Override
		public int compare(LinkedList<Edge<String, Double>> o1, LinkedList<Edge<String, Double>> o2) {
			double cost1 = 0, cost2 = 0;
			for (Edge<String, Double> edge : o1) {
				cost1 += edge.getCost();
			}
			for (Edge<String, Double> edge : o2) {
				cost2 += edge.getCost();
			}
			
			return Double.compare(cost1, cost2);
		}
		
	}
}