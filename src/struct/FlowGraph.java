package struct;

import action.impl.LoadGraphAction;
import ford.FordFulkersonError;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extension of {@link Graph} containing specific additional implementations relating to
 * timestamped deletions & max-flow solution processing
 *
 * @param <E> Generic type of {@link Graph}
 */
public class FlowGraph<E> extends Graph<E> {

    private LinkedStack<FlowEdgeDeletion<E>> recentDeletions = new LinkedStack<>();
    private Map<E, Map<E, Double>> flowMap = new HashMap<>();
    private double maxFlow;
    private boolean solved = false;
    private Pair<E, E> solvedVertices;

    private FordFulkersonError solutionError;

    @Override
    public void addEdge(E source, E dest, double cost) {
        solved = false;
        super.addEdge(source, dest, cost);
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Pair<E, E> getSolvedVertices() {
        return solvedVertices;
    }

    public void setSolvedVertices(Pair<E, E> solvedVertices) {
        this.solvedVertices = solvedVertices;
    }

    public double getMaxFlow() {
        return maxFlow;
    }

    public void setMaxFlow(double maxFlow) {
        this.maxFlow = maxFlow;
    }

    public FordFulkersonError getSolutionError() {
        return solutionError;
    }

    public void setSolutionError(FordFulkersonError solutionError) {
        this.solutionError = solutionError;
    }

    public Map<E, Map<E, Double>> getFlowMap() {
        return flowMap;
    }

    /**
     * Prints flow network solution to a Writer
     * @param writer writer to write to
     */
    public void printWithFlows(Writer writer) {
        try {
            for (E node : vertexSet.keySet()) {
                for (E other : vertexSet.get(node).adjList.keySet()) {

                    double flow = flowMap.get(node).get(other),
                            capacity = vertexSet.get(node).adjList.get(other).second;

                    writer.write(String.format("%s->%s (%s/%s) %.2f%% capacity\n",
                            node, other, flow, capacity, (100D * flow / capacity))
                    );
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean remove(E start, E end) {
        solved = false;
        double cost = vertexSet.getOrDefault(start, new Vertex<E>()).adjList.getOrDefault(end, new Pair<>(new Vertex<E>(), 0D)).second;
        return super.remove(start, end) && registerDeletion(start, end, cost, System.currentTimeMillis());
    }

    /**
     * Registeres a deletion
     *
     * @return
     */
    public boolean registerDeletion(E src, E dst, double cost, long millis) {
        return recentDeletions.push(new FlowEdgeDeletion<E>(src, dst, cost, millis));
    }

    /**
     * Returns whether or not a deletion has occured
     *
     * @return
     */
    public boolean hasRecentRemoval() {
        return !recentDeletions.isEmpty();
    }

    /**
     * Pops the most recent removal from the deletions stack and undoes it
     *
     * @return if the most recent deletion could be undone
     */
    public boolean undoRecentRemoval() {
        if (hasRecentRemoval()) {
            FlowEdgeDeletion<E> deletion = recentDeletions.pop();
            E src = deletion.getSource();
            E dst = deletion.getDestination();
            double cost = deletion.getCost();
            long ts = deletion.getMillis();

            addEdge(src, dst, cost);
            System.out.println(String.format("Restored pipe {%s->%s (%s)} from deletion %.2f seconds ago",
                    src, dst, cost, (System.currentTimeMillis() - ts) / 1000D));
            return true;
        }

        return false;
    }

    /**
     * Writes solution adjacency list to a file
     *
     * @param fileName filepath
     * @return success
     */
    public boolean saveSolution(String fileName) {
        StringWriter writer = new StringWriter();
        printWithFlows(writer);

        try {
            writer.close();
            Files.write(Paths.get(fileName), writer.toString().getBytes());
        } catch (IOException e) {
            return false;
        }

        return true;

    }

    // Extend serialize() to also store recently deleted pipes
    @Override
    public String serialize() {
        List<FlowEdgeDeletion> deletions = new ArrayList<>();
        recentDeletions.forEach(deletions::add);

        return super.serialize()
                + "\n"
                + LoadGraphAction.MAGIC_DELETION_DELIMITER + deletions.stream().map(FlowEdgeDeletion::serialize).collect(Collectors.joining(","));
    }
}
