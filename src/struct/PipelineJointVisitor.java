package struct;

public class PipelineJointVisitor implements Visitor<String> {
    @Override
    public void visit(String obj) {
        System.out.println("Pipeline joint: " + obj);
    }
}
