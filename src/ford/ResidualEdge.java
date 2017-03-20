package ford;

/**
 * Represents an edge of a residual graph
 *
 * Each edge has a pointer to its reverse
 * @param <E> Generic type of Graph
 */
public final class ResidualEdge<E> {
    private final E start;
    private final E end;
    private final boolean residual;
    private ResidualEdge<E> reverse;
    private double capacity;

    public ResidualEdge(E start, E end, double capacity, boolean actual) {
        this.start = start;
        this.end = end;
        this.capacity = capacity;
        this.residual = !actual;
    }

    public E getStart() {
        return start;
    }

    public E getEnd() {
        return end;
    }

    public double getCapacity() {
        return capacity;
    }

    public boolean isResidual() {
        return residual;
    }

    public ResidualEdge<E> getReverse() {
        return reverse;
    }

    public void setReverse(ResidualEdge<E> reverse) {
        this.reverse = reverse;
    }

    /**
     * Pushes flow through the edge, and its reverse
     *
     * If the amount to be pushed is negative, it will add to its reverse
     *
     *
     * @param amount flow to add
     */
    public void addFlow(double amount) {
        if (amount < 0) {
            reverse.addFlow(-amount);
            return;
        }

        capacity -= amount;
        reverse.capacity += amount;
    }

}
