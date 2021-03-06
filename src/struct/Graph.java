package struct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;


//--- Graph class ------------------------------------------------------
public class  Graph<E> {
    // the graph data is all here --------------------------
    public HashMap<E, Vertex<E>> vertexSet;

    // public graph methods --------------------------------
    public Graph() {
        vertexSet = new HashMap<E, Vertex<E>>();
    }

    public void addEdge(E source, E dest, double cost) {
        Vertex<E> src, dst;

        // put both source and dest into vertex list(s) if not already there
        src = addToVertexSet(source);
        dst = addToVertexSet(dest);

        // add dest to source's adjacency list
        src.addToAdjList(dst, cost);
        //dst.addToAdjList(src, cost); // ADD THIS IF UNDIRECTED GRAPH
    }

    public void addEdge(E source, E dest, int cost) {
        addEdge(source, dest, (double) cost);
    }

    // adds vertex with x in it, and always returns ref to it
    public Vertex<E> addToVertexSet(E x) {
        Vertex<E> retVal = null;
        Vertex<E> foundVertex;

        // find if Vertex already in the list:
        foundVertex = vertexSet.get(x);

        if (foundVertex != null) // found it, so return it
        {
            return foundVertex;
        }

        // the vertex not there, so create one
        retVal = new Vertex<E>(x);
        vertexSet.put(x, retVal);

        return retVal;   // should never happen
    }

    public boolean remove(E start, E end) {
        Vertex<E> startVertex = vertexSet.get(start);
        boolean removedOK = false;

        if (startVertex != null) {
            Pair<Vertex<E>, Double> endPair = startVertex.adjList.remove(end);
            removedOK = endPair != null;
        }
       /*// Add if UNDIRECTED GRAPH:
        Vertex<E> endVertex = vertexSet.get(end);
		if( endVertex != null )
		{
			Pair<Vertex<E>, Double> startPair = endVertex.adjList.remove(start);
			removedOK = startPair!=null ;
		}
		*/

        return removedOK;
    }

    public void showAdjTable() {
        Iterator<Entry<E, Vertex<E>>> iter;

        System.out.println("------------------------ ");
        iter = vertexSet.entrySet().iterator();
        while (iter.hasNext()) {
            (iter.next().getValue()).showAdjList();
        }
        System.out.println();
    }

    /**
     * Returns true if a vertex exists in the vertexSet
     * @param vertex object to scan for
     * @return existence of edge
     */
    public boolean contains(E vertex) {
        return vertexSet.containsKey(vertex);
    }

    public void clear() {
        vertexSet.clear();
    }

    // reset all vertices to unvisited
    public void unvisitVertices() {
        Iterator<Entry<E, Vertex<E>>> iter;

        iter = vertexSet.entrySet().iterator();
        while (iter.hasNext()) {
            iter.next().getValue().unvisit();
        }
    }

    /**
     * Breadth-first traversal from the parameter startElement
     */
    public void breadthFirstTraversal(E startElement, Visitor<E> visitor) {
        unvisitVertices();

        Vertex<E> startVertex = vertexSet.get(startElement);
        breadthFirstTraversalHelper(startVertex, visitor);
    }

    /**
     * Depth-first traversal from the parameter startElement
     */
    public void depthFirstTraversal(E startElement, Visitor<E> visitor) {
        unvisitVertices();

        Vertex<E> startVertex = vertexSet.get(startElement);
        depthFirstTraversalHelper(startVertex, visitor);
    }

    protected void breadthFirstTraversalHelper(Vertex<E> startVertex,
                                               Visitor<E> visitor) {
        LinkedQueue<Vertex<E>> vertexQueue = new LinkedQueue<>();
        E startData = startVertex.getData();

        startVertex.visit();
        visitor.visit(startData);
        vertexQueue.enqueue(startVertex);
        while (!vertexQueue.isEmpty()) {
            Vertex<E> nextVertex = vertexQueue.dequeue();
            Iterator<Entry<E, Pair<Vertex<E>, Double>>> iter =
                    nextVertex.iterator(); // iterate adjacency list

            while (iter.hasNext()) {
                Entry<E, Pair<Vertex<E>, Double>> nextEntry = iter.next();
                Vertex<E> neighborVertex = nextEntry.getValue().first;
                if (!neighborVertex.isVisited()) {
                    vertexQueue.enqueue(neighborVertex);
                    neighborVertex.visit();
                    visitor.visit(neighborVertex.getData());
                }
            }
        }
    } // end breadthFirstTraversalHelper

    public void depthFirstTraversalHelper(Vertex<E> startVertex, Visitor<E> visitor) {
        // YOU COMPLETE THIS (USE THE ALGORITHM GIVEN FOR LESSON 11 EXERCISE)
        startVertex.visit();

        E startData = startVertex.getData();
        visitor.visit(startData);

        for (E adj : startVertex.adjList.keySet()) {
            Vertex<E> adjacent = startVertex.adjList.get(adj).first;

            if (!adjacent.isVisited()) {
                depthFirstTraversalHelper(adjacent, visitor);
            }
        }
    }

    public String serialize() {
        return vertexSet.entrySet().stream().map(
                e -> e.getKey() + "|" + e.getValue().adjList.values().stream().map(
                        p -> p.first.data + "|" + p.second)
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining("\n"));
    }

    public boolean saveGraph(String fileName) {
        String payload = serialize();

        try {
            Files.write(Paths.get(fileName), payload.getBytes());
        } catch (IOException e) {
            return false;
        }

        return true;

    }
}
