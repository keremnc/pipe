package ford;

import struct.Graph;
import struct.Pair;
import struct.Vertex;

import java.util.*;

public final class ResidualGraph<T> implements Iterable<T> {
    public static final class Edge<T> {
        private final T start;
        private final T end;
        private final boolean isResidual;
        private Edge<T> reverse;
        private double capacity;

        public T getStart() {
            return start;
        }
        public T getEnd() {
            return end;
        }
        public double getCapacity() {
            return capacity;
        }

        public boolean isResidual() {
            return isResidual;
        }

        public void addFlow(double amount) {
            if (amount < 0) {
                reverse.addFlow(-amount);
                return;
            }

            capacity -= amount;
            reverse.capacity += amount;
        }

        public Edge<T> getReverse() {
            return reverse;
        }

        private void setReverse(Edge<T> reverse) {
            this.reverse = reverse;
        }

        private Edge(T start, T end, double capacity, boolean isOriginal) {
            this.start = start;
            this.end = end;
            this.capacity = capacity;
            this.isResidual = !isOriginal;
        }
    }

    private final Map<T, List<Edge<T>>> graph = new HashMap<>();

    public ResidualGraph(Graph<T> g) {

        for (T node : g.vertexSet.keySet()) {
            graph.put(node, new ArrayList<>());
        }

        for (T node : g.vertexSet.keySet()) {
            Vertex<T> vert  = g.vertexSet.get(node);
            for (Pair<Vertex<T>, Double> adj : vert.adjList.values()) {

                Edge<T> forward = new Edge<T>(node, adj.first.data, adj.second.intValue(),true);
                Edge<T> reverse = new Edge<T>(adj.first.data, node, 0,false);

                forward.setReverse(reverse);
                reverse.setReverse(forward);

                graph.get(node).add(forward);
                graph.get(adj.first.data).add(reverse);
            }
        }
    }

    public Iterator<T> iterator() {
        return graph.keySet().iterator();
    }

    public List<Edge<T>> edgesFrom(T node) {
        List<Edge<T>> edges = graph.get(node);

        return Collections.unmodifiableList(edges);
    }
}