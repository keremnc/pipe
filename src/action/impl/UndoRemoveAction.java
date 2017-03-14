package action.impl;

import action.NoArgsAction;
import action.result.ActionResult;
import data.PipeSystem;
import io.PipelineContext;

public class UndoRemoveAction extends NoArgsAction {

    @Override
    public String getName() {
        return "Undo removal";
    }

    @Override
    public ActionResult act(PipelineContext<String> context) {
        PipeSystem<String> graph = context.getSystem();

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
