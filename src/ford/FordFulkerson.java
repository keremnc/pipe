package ford;

import struct.FlowGraph;
import struct.LinkedStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class FordFulkerson {

    public static <T> void findMaxFlow(FlowGraph<T> g, T s, T t) {
        int maxFlow = 0;

        if (s.equals(t)) return;
        ResidualGraph<T> gResidual = new ResidualGraph<T>(g);

        LinkedStack<ResidualGraph.Edge<T>> path;
        while ((path = findPathRec(s, t, gResidual, new HashSet<T>())) != null) {
            maxFlow += augmentPath(path);
        }

        for (T node: gResidual) {
            for (ResidualGraph.Edge<T> edge: gResidual.edgesFrom(node)) {
                if (edge.isResidual()) {
                    g.getFlowMap().computeIfAbsent(edge.getEnd(), p -> new HashMap<>()).put(edge.getStart(), edge.getCapacity() + 0D);
                }
            }
        }

        g.setMaxFlow(maxFlow);
        g.setSolved(true);
    }

    private static <T> LinkedStack<ResidualGraph.Edge<T>> findPathRec(T start, T dest,
                                                                ResidualGraph<T> graph,
                                                                Set<T> visited) {
        if (visited.contains(start)) return null;

        visited.add(start);
        if (start.equals(dest)) {
            return new LinkedStack<>();
        }
        for (ResidualGraph.Edge<T> edge: graph.edgesFrom(start)) {
            if (edge.getCapacity() == 0) continue;

            LinkedStack<ResidualGraph.Edge<T>> result = findPathRec(edge.getEnd(), dest,
                    graph, visited);
            if (result != null) {
                result.push(edge);
                return result;
            }
        }
        return null;
    }

    private static <T> double augmentPath(LinkedStack<ResidualGraph.Edge<T>> path) {
        double capacity = Double.MAX_VALUE;
        for (ResidualGraph.Edge<T> edge: path)
            capacity = Math.min(capacity, edge.getCapacity());

        for (ResidualGraph.Edge<T> edge: path) {
            edge.addFlow(capacity);
        }

        return capacity;
    }
}