package struct;

import action.impl.LoadGraphAction;

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

public class FlowGraph<E> extends Graph<E> {

    private LinkedStack<FlowEdgeDeletion<E>> recentDeletions = new LinkedStack<>();
    private Map<E, Map<E, Double>> flowMap = new HashMap<>();
    private double maxFlow;
    private boolean solved = false;

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

    public double getMaxFlow() {
        return maxFlow;
    }

    public void setMaxFlow(double maxFlow) {
        this.maxFlow = maxFlow;
    }

    public Map<E, Map<E, Double>> getFlowMap() {
        return flowMap;
    }

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

    public boolean registerDeletion(E src, E dst, double cost, long millis) {
        return recentDeletions.push(new FlowEdgeDeletion<E>(src, dst, cost, millis));
    }

    public boolean hasRecentRemoval() {
        return !recentDeletions.isEmpty();
    }

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
