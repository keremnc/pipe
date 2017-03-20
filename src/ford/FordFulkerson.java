package ford;

import struct.FlowGraph;
import struct.LinkedStack;
import struct.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class that applies Ford-Fulkerson algorithm
 */
public final class FordFulkerson {

    /**
     * Synchronously runs the max-flow algorithm on a graph; transforms graphs
     * @param pipe input flow network
     * @param source start vertex
     * @param sink destination vertex
     * @param <T> Generic type of Graph
     */
    public static <T> void findMaxFlow(FlowGraph<T> pipe, T source, T sink) {
        double maxFlow = 0;

        pipe.setSolvedVertices(null);
        pipe.setSolved(false);
        pipe.setSolutionError(FordFulkersonError.UNKNOWN);
        // Initial pipe's solution state to unsolved with unknown error
        // If an exception occurs during processing, this will be the solution state
        // Program is single-threaded, no need to worry about concurrency here


        if (source.equals(sink)) { // src & dst are identical
            pipe.setSolutionError(FordFulkersonError.SRC_AND_DEST_IDENTICAL);
            return;
        }

        if (!(pipe.contains(source) && pipe.contains(sink))) {
            pipe.setSolutionError(FordFulkersonError.MISSING_VERTEX);
            return;
        }

        ResidualGraph<T> gResidual = new ResidualGraph<T>(pipe);

        LinkedStack<ResidualEdge<T>> path;
        boolean pathFound = false;

        // For every path from start to dest where flow can be pushed through
        while ((path = dfsFindPath(source, sink, gResidual, new HashSet<T>())) != null) {
            maxFlow += augmentPath(path);
            pathFound = true;
        }

        if (!pathFound) { // Path could not be found between src & dst
            pipe.setSolutionError(FordFulkersonError.NO_PATH_EXIST);
            return;
        }

        // Transform input graph with calculated residual flow values from residual graphs
        for (T node : gResidual) {
            for (ResidualEdge<T> residualEdge : gResidual.edgesFrom(node)) {
                if (residualEdge.isResidual()) {
                    pipe.getFlowMap().computeIfAbsent(residualEdge.getEnd(), p -> new HashMap<>()).put(residualEdge.getStart(), residualEdge.getCapacity() + 0D);
                }
            }
        }

        pipe.setSolvedVertices(new Pair<>(source, sink));
        pipe.setMaxFlow(maxFlow);
        pipe.setSolved(true);
        pipe.setSolutionError(null);
    }

    /**
     * Recursive DFS algo to find augmenting paths where flow can be passed through
     * @param src source vertex
     * @param dst destination vertex
     * @param graph residual flow network
     * @param visited set of visited vertices
     * @param <T> Generic type of Graph
     * @return
     */
    private static <T> LinkedStack<ResidualEdge<T>> dfsFindPath(T src, T dst,
                                                                ResidualGraph<T> graph,
                                                                Set<T> visited) {
        if (visited.contains(src)) return null;

        visited.add(src);
        if (src.equals(dst)) {
            return new LinkedStack<>();
        }

        for (ResidualEdge<T> residualEdge : graph.edgesFrom(src)) {
            if (residualEdge.getCapacity() == 0) continue; // Only process paths where flow can be pushed through

            LinkedStack<ResidualEdge<T>> result = dfsFindPath(residualEdge.getEnd(), dst,
                    graph, visited);
            if (result != null) {
                result.push(residualEdge);
                return result;
            }
        }
        return null;
    }

    private static <T> double augmentPath(LinkedStack<ResidualEdge<T>> path) {
        double capacity = Double.MAX_VALUE;
        for (ResidualEdge<T> residualEdge : path)
            capacity = Math.min(capacity, residualEdge.getCapacity());

        for (ResidualEdge<T> residualEdge : path) {
            residualEdge.addFlow(capacity);
        }

        return capacity;
    }
}