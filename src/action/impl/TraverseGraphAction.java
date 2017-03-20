package action.impl;

import action.UserAction;
import action.ActionResult;
import data.PipelineContext;
import struct.FlowGraph;
import struct.StringVisitor;

/**
 * Action to print graph, traversed w/ either DFS or BFS
 */
public class TraverseGraphAction implements UserAction {
    private static final StringVisitor DISPLAY_VISITOR = new StringVisitor("Flow joint");

    @Override
    public ActionResult act(PipelineContext<String> context) {
        FlowGraph<String> graph = context.getSystem();
        String vertex = context.getInputParams()[1];

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
