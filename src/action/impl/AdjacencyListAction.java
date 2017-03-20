package action.impl;

import action.UserAction;
import action.ActionResult;
import data.PipelineContext;
import struct.FlowGraph;

/**
 * Action to print the adjacency list of the {@link FlowGraph}
 */
public class AdjacencyListAction implements UserAction {

    @Override
    public String getName() {
        return "Display graph";
    }

    @Override
    public ActionResult act(PipelineContext<String> context) {
        FlowGraph<String> graph = context.getSystem();
        graph.showAdjTable();
        return new ActionResult(true, null);
    }

    @Override
    public boolean requiresGraphLoaded() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Prints adjacency list";
    }
}
