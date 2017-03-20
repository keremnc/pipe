package struct;

/**
 * {@link Visitor} implementation that prints a String vertex as is
 */
public class StringVisitor implements Visitor<String> {

    private String label;

    public StringVisitor(String label) {
        this.label = label;
    }

    @Override
    public void visit(String obj) {
        System.out.println(label + ": " + obj);
    }
}
