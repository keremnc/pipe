package action.impl;

import action.NoArgsAction;
import action.result.ActionResult;
import struct.FlowGraph;
import data.PipelineContext;

public class AdjacencyListAction extends NoArgsAction {

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
