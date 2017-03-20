package action.impl;

import action.UserAction;
import action.ActionResult;
import struct.FlowGraph;
import data.PipelineContext;

/**
 * Action to save {@link FlowGraph} to a file
 */
public class SaveGraphAction implements UserAction {

    @Override
    public ActionResult act(PipelineContext<String> context) {
        String fileName = context.getInputParams()[0];

        FlowGraph<String> graph = context.getSystem();

        if (graph.saveGraph(fileName)) {
            return new ActionResult(true, "Saved graph to '" + fileName + "'");
        }

        return new ActionResult(false, "Unable to save graph to file...");

    }

    @Override
    public boolean requiresGraphLoaded() {
        return true;
    }

    @Override
    public String getName() {
        return "Save graph";
    }

    @Override
    public String getDescription() {
        return "Save graph to file";
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"filename"};
    }
}
