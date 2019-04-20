package finder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import core.Edge;
import core.Graph;
import core.Node;

import java.util.PriorityQueue;
import java.util.Scanner;

class CampusPaths {
	private Graph<NodeTuple, Double> graph;
	
	public CampusPaths() {
		graph = new Graph<NodeTuple, Double>();
	}
	
	private void readData(String node_filename, String edge_filename) {
        
		String line;
        String token = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(node_filename))) {
            
        	while ((line = br.readLine()) != null) {

            	String[] data = line.split(token);
            	NodeTuple nt = new NodeTuple(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                graph.addNode(new Node<NodeTuple, Double>(nt));
        	}

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(edge_filename))) {
            
        	while ((line = br.readLine()) != null) {

            	String[] data = line.split(token);
            	Node<NodeTuple, Double> n1 = null, n2 = null;
            	
            	Iterator<Entry<NodeTuple, Node<NodeTuple, Double>>> iterator = graph.getNodes();
            	while (iterator.hasNext()) {
            		Entry<NodeTuple, Node<NodeTuple, Double>> entry = iterator.next();
            		if (entry.getKey().getId() == Integer.parseInt(data[0])) {
						n1 = entry.getValue();
					}
            		if (entry.getKey().getId() == Integer.parseInt(data[1])) {
						n2 = entry.getValue();
					}
					
				}
            	
            	double dist = Math.sqrt(Math.pow(n1.getData().getX() - n2.getData().getX(), 2)
            			+ Math.pow(n1.getData().getY() - n2.getData().getY(), 2));
            	
                graph.addEdge(n1, n2, dist);
                graph.addEdge(n2, n1, dist);
        	}

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private LinkedList<Edge<NodeTuple, Double>> getShortestPath(Node<NodeTuple, Double> startingNode, Node<NodeTuple, Double> destinationNode) {
		
			if (startingNode.equals(destinationNode)) return new LinkedList<Edge<NodeTuple, Double>>();
		
			PriorityQueue<LinkedList<Edge<NodeTuple, Double>>> activePaths = new PriorityQueue<LinkedList<Edge<NodeTuple,Double>>>(5, new CostComparator());
			ArrayList<Node<NodeTuple, Double>> fininshedNodes = new ArrayList<Node<NodeTuple,Double>>();
			
		    // Initially we only know of the path from start to itself, which has a cost
		    // of zero because it contains no edges.
			Iterator<Edge<NodeTuple, Double>> initEdgesIterator = startingNode.getEdges();
			
			while (initEdgesIterator.hasNext()) {
				LinkedList<Edge<NodeTuple, Double>> initPath = new LinkedList<Edge<NodeTuple,Double>>();
				initPath.add(initEdgesIterator.next());
				activePaths.add(initPath);
			}
		    while (!activePaths.isEmpty()) {
		        
		    	// minPath is the lowest­cost path in active and is the minimum­cost
		        // path for some node
		        LinkedList<Edge<NodeTuple, Double>> minPath = activePaths.poll();
		        Node<NodeTuple, Double> minDest = minPath.peekLast().getEndingNode();
		        if (minDest.equals(destinationNode)) {
		        	graph.unvisitAll();
		        	if (minPath.isEmpty()) {
						break;
					}
		        	return minPath;
		        }
		        if (fininshedNodes.contains(minDest)) {
		        	continue;
		        }
		        
		        Iterator<Edge<NodeTuple, Double>> nextEdgeIterator = minDest.getEdges();
		        while (nextEdgeIterator.hasNext()) {
					Edge<NodeTuple, Double> nextEdge = nextEdgeIterator.next();
					Node<NodeTuple, Double> nextNode = nextEdge.getEndingNode();
					
					if (!fininshedNodes.contains(nextNode) && !minDest.isVisited()) {
						
						// If we don't know the minimum­cost path from start to child, examine the path we've just found
						LinkedList<Edge<NodeTuple, Double>> newPath = new LinkedList<Edge<NodeTuple,Double>>(minPath);
						newPath.add(nextEdge);
						activePaths.add(newPath);
					}
					
				}
				fininshedNodes.add(minDest);
				minDest.visit();
		    }
		    
			return null;
	}
	
	private void translate() {
		
		Scanner input = new Scanner(System.in);
		while (input.hasNext()) {
			String token = input.nextLine();
			char command;
			if (token.isEmpty()) {
				command = '\0';
			} else {
				command = token.trim().charAt(0);
			}
			
			switch (command) {
			case 'b':
				listBuilding();
				continue;
			case 'r':
				printPath(input);
				continue;
			case 'q':
				return;
			case 'm':
				printCommand();
				continue;
			default:
				unknown();
				continue;
			}
		}
	}
	
	private void printCommand() {
		System.out.println("b - lists all buildings (only buildings) in the form name,id in lexicographic (alphabetical) order of name.");
		System.out.println("r - prompts the user for the ids or names of two buildings (only buildings!) and prints directions for the shortest route between them.");
		System.out.println("q - quits the program.");
		System.out.println("m - prints a menu of all commands.");
	}

	private void printPath(Scanner input) {
		Node<NodeTuple, Double> node1 = null, node2 = null;
		String token1 = null, token2 = null;
		
		// read in the first node
		System.out.print("First building id/name, followed by Enter: ");
		if (input.hasNext()) {
			token1 = input.nextLine();
			node1 = readNode(token1);
		}
		
		// read in the second node
		System.out.print("Second building id/name, followed by Enter: ");
		if (input.hasNext()) {
			token2 = input.nextLine();
			node2 = readNode(token2);
		}
		
		// omit intersection
		if (node1 != null && node1.getData().getName().equals("")) {
			node1 = null;
		}
		if (node2 != null && node2.getData().getName().equals("")) {
			node2 = null;
		}
		
		if (node1 == null || node2 == null) {
			if (!token1.equals(token2)) {
				if (node1 == null) {
					System.out.println("Unknown building: [" + token1 + "]");
				}
				if (node2 == null) {
					System.out.println("Unknown building: [" + token2 + "]");
				}
			} else {
				System.out.println("Unknown building: [" + token1 + "]");
			}
			return;
		}
		
		LinkedList<Edge<NodeTuple, Double>> path = getShortestPath(node1, node2);
		
		if (path != null) {
			Iterator<Edge<NodeTuple, Double>> pathIterator = path.iterator();
			double dist = 0;
			System.out.println("Path from " + node1.getData().getName() + " to " + node2.getData().getName() + ":");
			while (pathIterator.hasNext()) {
				Edge<NodeTuple, Double> edge = pathIterator.next();
				NodeTuple endingNodeData = edge.getEndingNode().getData();
				String label = endingNodeData.getName().equals("")?"Intersection "+endingNodeData.getId():endingNodeData.getName();
				System.out.println("\tWalk " + parseDirection(edge) + " to (" + label + ")");
				dist += edge.getCost();
			}
			System.out.println(String.format("Total distance: %.3f pixel units.", dist));
		} else {
			System.out.println("There is no path from " + token1 + " to " + token2 + ".");
		}
		
		
	}

	private Node<NodeTuple, Double> readNode(String token) {
		Node<NodeTuple, Double> node = null;
		try {
	         int id = Integer.parseInt(token);
				
				Iterator<Entry<NodeTuple, Node<NodeTuple, Double>>> iterator = graph.getNodes();
				while (iterator.hasNext()) {
					Entry<NodeTuple, Node<NodeTuple, Double>> entry = iterator.next();
					if (entry.getKey().getId() == id) {
						node = entry.getValue();
					}
				}
				
	      }	catch(NumberFormatException e) {
	    	  String name = token;
				
				Iterator<Entry<NodeTuple, Node<NodeTuple, Double>>> iterator = graph.getNodes();
				while (iterator.hasNext()) {
					Entry<NodeTuple, Node<NodeTuple, Double>> entry = iterator.next();
					if (entry.getKey().getName().equals(name)) {
						node = entry.getValue();
					}
				}
	      }
		
		return node;
	}
	
	private Direction parseDirection(Edge<NodeTuple, Double> edge) {
		Node<NodeTuple, Double> node1 = edge.getSourcingNode();
		Node<NodeTuple, Double> node2 = edge.getEndingNode();
		
		double x = node2.getData().getX() - node1.getData().getX();
		double y = node1.getData().getY() - node2.getData().getY();
		
		double angle = Math.atan2(x, y)*(180/Math.PI);
		
		if ((0 <= angle && angle < 22.5) || (-22.5 <= angle && angle <= 0)) {
			return Direction.North;
		}
		if (22.5 <= angle && angle < 67.5) {
			return Direction.NorthEast;
		}
		if (67.5 <= angle && angle < 112.5) {
			return Direction.East;
		}
		if (112.5 <= angle && angle < 157.5) {
			return Direction.SouthEast;
		}
		if ((157.5 <= angle && angle <= 180) || (-180 <= angle && angle < -157.5)) {
			return Direction.South;
		}
		if (-157.5 <= angle && angle < -112.5) {
			return Direction.SouthWest;
		}
		if (-112.5 <= angle && angle < -67.5) {
			return Direction.West;
		}
		if (-67.5 <= angle && angle < -22.5) {
			return Direction.NorthWest;
		}
		return null;
	}
	
	
	private void listBuilding() {
		Iterator<Entry<NodeTuple, Node<NodeTuple, Double>>> iterator = graph.getNodes();
		ArrayList<String> list = new ArrayList<String>();
		while (iterator.hasNext()) {
			Entry<NodeTuple, Node<NodeTuple, Double>> entry = iterator.next();
			
			if (entry.getKey().getName().equals("")) continue;
			
			list.add(entry.getKey().getName()+","+entry.getKey().getId());
		}
		Collections.sort(list);
		for (String s : list) {
			System.out.println(s);
		}
	}
	
	private void unknown() {
		System.out.println("Unknown option");
	}
	
	class CostComparator implements Comparator<LinkedList<Edge<NodeTuple, Double>>> {

		@Override
		public int compare(LinkedList<Edge<NodeTuple, Double>> o1, LinkedList<Edge<NodeTuple, Double>> o2) {
			double cost1 = 0, cost2 = 0;
			for (Edge<NodeTuple, Double> edge : o1) {
				cost1 += edge.getCost();
			}
			for (Edge<NodeTuple, Double> edge : o2) {
				cost2 += edge.getCost();
			}
			
			return Double.compare(cost1, cost2);
		}
		
	}
	
	public static void main(String[] args) {
		CampusPaths campusPaths = new CampusPaths();
		campusPaths.readData("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");
		campusPaths.translate();
	}
}