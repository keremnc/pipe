package data;

public class PipeDeletion<E> {
    private E source;
    private E destination;
    private double cost;
    private long millis;

    public PipeDeletion(E source, E destination, double cost, long millis) {
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
