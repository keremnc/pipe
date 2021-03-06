package action.impl;

import action.UserAction;
import action.ActionResult;
import data.PipelineContext;
import struct.FlowGraph;

/**
 * Action to write solved max-flow graph to a file
 */
public class WriteSolutionAction implements UserAction {

    @Override
    public ActionResult act(PipelineContext<String> context) {
        String fileName = context.getInputParams()[0];

        FlowGraph<String> graph = context.getSystem();

        if (!graph.isSolved()) {
            return new ActionResult(false, "You must solve the graph before writing!");
        }

        if (graph.saveSolution(fileName)) {
            return new ActionResult(true, "Saved flow network to '" + fileName + "'");
        }

        return new ActionResult(false, "Unable to save flow network to file...");

    }

    @Override
    public boolean requiresGraphLoaded() {
        return true;
    }

    @Override
    public String getName() {
        return "Save solution";
    }

    @Override
    public String getDescription() {
        return "Save solved flow network to disk";
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"filename"};
    }
}
