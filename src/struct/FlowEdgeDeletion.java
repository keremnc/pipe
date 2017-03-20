package struct;

/**
 * Timestamped data structure representing a deleted edge from a graph
 * @param <E> Generic type of {@link Graph}
 */
public class FlowEdgeDeletion<E> {
    private E source;
    private E destination;
    private double cost;
    private long millis;

    public FlowEdgeDeletion(E source, E destination, double cost, long millis) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.millis = millis;
    }

    public double getCost() {
        return cost;
    }

    public E getSource() {
        return source;
    }

    public E getDestination() {
        return destination;
    }

    public long getMillis() {
        return millis;
    }

    public String serialize() {
        return String.format("%s|%s|%f|%d", getSource(), getDestination(), getCost(), getMillis());
    }
}
