package ford;

import struct.Graph;
import struct.Pair;
import struct.Vertex;

import java.util.*;

/**
 * Represents a residual graph of a graph where for all edges in the original graph there exists two residual edges;
 * one has capacity of original capacity - original flow, where as the other has capacity of original flow
 *
 * Graph is represented with a Map of vertex <-> edge list
 * @param <E> Generic type of {@link Graph}
 */
public final class ResidualGraph<E> implements Iterable<E> {

    private final Map<E, List<ResidualEdge<E>>> graph = new HashMap<>();

    /**
     * Creates a residual graph copy of a {@link Graph}
     * @param g graph to copy
     */
    public ResidualGraph(Graph<E> g) {

        for (E node : g.vertexSet.keySet()) {
            graph.put(node, new ArrayList<>());
        }

        for (E node : g.vertexSet.keySet()) {
            Vertex<E> vert = g.vertexSet.get(node);
            for (Pair<Vertex<E>, Double> adj : vert.adjList.values()) {

                ResidualEdge<E> forward = new ResidualEdge<E>(node, adj.first.data, adj.second, true);
                ResidualEdge<E> reverse = new ResidualEdge<E>(adj.first.data, node, 0, false);

                forward.setReverse(reverse);
                reverse.setReverse(forward);

                graph.get(node).add(forward);
                graph.get(adj.first.data).add(reverse);
            }
        }
    }

    public Iterator<E> iterator() {
        return graph.keySet().iterator();
    }

    /**
     * Gets list of edges spanning out from one vertex
     * @param node vertex
     * @return list of edges
     */
    public List<ResidualEdge<E>> edgesFrom(E node) {
        return graph.get(node);
    }
}