package action.impl;

import action.UserAction;
import action.ActionResult;
import data.PipelineContext;
import struct.FlowGraph;

/**
 * Action to remove edge from {@link struct.Graph}
 */
public class RemovePipeAction implements UserAction {

    @Override
    public ActionResult act(PipelineContext<String> context) {
        FlowGraph<String> graph = context.getSystem();

        String srcStr = context.getInputParams()[0];
        String dstStr = context.getInputParams()[1];
        if (context.getSystem().remove(srcStr, dstStr)) {
            return new ActionResult(true, String.format("Pipe {%s->%s} removed!", srcStr, dstStr));
        }

        return new ActionResult(false, String.format("Unable to remove pipe {%s->%s} (Does it exist?)", srcStr, dstStr));
    }

    @Override
    public boolean requiresGraphLoaded() {
        return true;
    }

    @Override
    public String getName() {
        return "Remove pipe";
    }

    @Override
    public String getDescription() {
        return "Remove a pipe from the graph";
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"source", "destination"};
    }
}
