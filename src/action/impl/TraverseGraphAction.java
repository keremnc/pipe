package action.impl;

import action.WildcardArgsAction;
import action.result.ActionResult;
import struct.PipeSystem;
import struct.PipelineJointVisitor;
import data.PipelineContext;

public class TraverseGraphAction extends WildcardArgsAction {
    private static final PipelineJointVisitor DISPLAY_VISITOR = new PipelineJointVisitor();

    @Override
    public ActionResult process(PipelineContext<String> context) {
        PipeSystem<String> graph = context.getSystem();
        String vertex = context.getInputParams()[1].toUpperCase();

        if (!graph.contains(vertex)) {
            return new ActionResult(false, "Pipe joint '" + vertex + "' does not exist...");
        }

        switch (context.getInputParams()[0].toLowerCase().charAt(0)) {
            case 'd':
                graph.depthFirstTraversal(vertex, DISPLAY_VISITOR);
                break;
            case 'b':
                graph.breadthFirstTraversal(vertex, DISPLAY_VISITOR);
                break;

            default:
                return new ActionResult(false, "Unable to recognize display mode: " + context.getInputParams()[0]);
        }
        return new ActionResult(true, null);
    }

    @Override
    public boolean requiresGraphLoaded() {
        return true;
    }

    @Override
    public String[] getParameterNames() {
        return new String[] {"<(D)FS | (B)FS>", "start joint"};
    }

    @Override
    public String getName() {
        return "Display graph";
    }

    @Override
    public String getDescription() {
        return "Displays graph with BFS or DFS";
    }
}
