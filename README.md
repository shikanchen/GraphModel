# GraphModel
The Pathfinder extension of Java. Easy to use to create node, edge in graphs and to find shortest path.

## How to Use
Create a graph

```Java
Graph myGraph = new Graph();
```

Fill nodes into the graph we just created
```Java
myGraph.addNode("I am a node!");
myGraph.addNode(new Node<String, String>("I am another node!"));
```

Connect the two nodes we created with an edge
```Java
myGraph.addEdge("I am a node!", "I am another node!", "I am an edge!");
myGraph.addEdge(new Node<String, String>("I am a node!"),
              new Node<String, String>("I am another node!"),
              "I am another edge!");
```

## Core Structure
- Node.java<T, V>
  - getData()
  - setData(T)
  - getSize()
  - getEdges()
  - isVisited()
  - visit()
  - unvisit()
  - connects(Node<T, V>, V)

- Edge.java<T, V>
  - setCost(V)
  - getCost()
  - getEndingNode()
  - getSourcingNode()

- Graph.java<T, V>
  - addEdge(Node<T, V>, Node<T, V>, V)
  - addEdge(T, T, V)
  - addNode(Node<T, V>)
  - addNode(T)
  - getNode(T t)
  - clear()
  - getSize()
  - unvisitAll()
  - getNodes()
  - getEdges()
  - getEdges(Node<T, V>, Node<T, V>)
  - getEdges(T, T)

## Supported Pathfinding Algorithm
- Depth First Search
- Breadth First Search
- Dijkstra's algorithm
