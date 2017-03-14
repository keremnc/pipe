package data;

import action.impl.LoadGraphAction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PipeSystem<E> extends Graph<E> {

    private LinkedStack<PipeDeletion<E>> recentDeletions = new LinkedStack();

    @Override
    public boolean remove(E start, E end) {
        double cost = vertexSet.getOrDefault(start, new Vertex<E>()).adjList.getOrDefault(end, new Pair<>(new Vertex<E>(), 0D)).second;
        return super.remove(start, end) && registerDeletion(start, end, cost, System.currentTimeMillis());
    }

    public boolean registerDeletion(E src, E dst, double cost, long millis) {
        return recentDeletions.push(new PipeDeletion<E>(src, dst, cost, millis));
    }

    public boolean hasRecentRemoval() {
        return !recentDeletions.isEmpty();
    }

    public boolean undoRecentRemoval() {
        if (hasRecentRemoval()) {
            PipeDeletion<E> deletion = recentDeletions.pop();
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

    // Extend serialize() to also store recently deleted pipes
    @Override
    public String serialize() {
        List<PipeDeletion> deletions = new ArrayList<>();
        recentDeletions.forEach(deletions::add);

        return super.serialize()
                + "\n"
                + LoadGraphAction.MAGIC_DELETION_DELIMITER + deletions.stream().map(PipeDeletion::serialize).collect(Collectors.joining(","));
    }
}
