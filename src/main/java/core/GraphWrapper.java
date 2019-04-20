package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Shikan Chen 
 */
public class GraphWrapper {
	Graph<String, String> graph;

	public GraphWrapper() {
		graph = new Graph<String, String>();
	}
	
	public GraphWrapper(Graph<String, String> graph) {
		this.graph = graph;
	}

	public void addNode(String data) {
		graph.addNode(data);
	}

	public void addEdge(String parentNode, String childNode, String edgeLabel) {
		graph.addEdge(parentNode, childNode, edgeLabel);
	}

	public Iterator<String> listNodes() {
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Entry<String, Node<String, String>>> it = graph.getNodes();
	      while (it.hasNext()) {
	          list.add(it.next().getValue().toString());
	      }

	      Collections.sort(list);
	      return list.iterator();
	}

	public Iterator<String> listChildren(String parentNode) {
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Edge<String, String>> it = graph.getNode(parentNode).getEdges();
	      while (it.hasNext()) {
	          list.add(it.next().toString());
	      }

	      Collections.sort(list);
	      return list.iterator();
	}
	
	public Iterator<Edge<String, String>> listChildren(Node<String, String> node) {
		ArrayList<Edge<String, String>> list = new ArrayList<Edge<String, String>>();
		Iterator<Edge<String, String>> it = node.getEdges();
		while (it.hasNext()) {
			list.add(it.next());
		}
		
		Collections.sort(list, new EdgeComparator());
		
		return list.iterator();
		
	}
	
	class EdgeComparator implements Comparator<Edge<String, String>> {
		
		@Override
		public int compare(Edge<String, String> o1, Edge<String, String> o2) {
			return o1.toString().compareTo(o2.toString());
		}
		
	}

}