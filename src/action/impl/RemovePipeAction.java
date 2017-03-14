package action.impl;

import action.WildcardArgsAction;
import action.result.ActionResult;
import data.PipeSystem;
import io.PipelineContext;

public class RemovePipeAction extends WildcardArgsAction {

    @Override
    public ActionResult process(PipelineContext<String> context) {
        PipeSystem<String> graph = context.getSystem();

        String srcStr = context.getInputParams()[0].toUpperCase();
        String dstStr = context.getInputParams()[1].toUpperCase();
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
