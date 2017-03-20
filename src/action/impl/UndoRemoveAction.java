package action.impl;

import action.UserAction;
import action.ActionResult;
import data.PipelineContext;
import struct.FlowGraph;

/**
 * Action to undo the most recent edge deletion
 */
public class UndoRemoveAction implements UserAction {

    @Override
    public String getName() {
        return "Undo removal";
    }

    @Override
    public ActionResult act(PipelineContext<String> context) {
        FlowGraph<String> graph = context.getSystem();

        if (graph.hasRecentRemoval()) {
            if (graph.undoRecentRemoval()) {
                return new ActionResult(true, null);
            }
            return new ActionResult(false, "Unable to undo recent removal...");

        }

        return new ActionResult(false, "Graph has no recent removal...");
    }

    @Override
    public boolean requiresGraphLoaded() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Undo previous removal(s)";
    }
}
