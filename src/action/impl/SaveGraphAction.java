package action.impl;

import action.WildcardArgsAction;
import action.result.ActionResult;
import struct.PipeSystem;
import data.PipelineContext;

public class SaveGraphAction extends WildcardArgsAction {

    @Override
    public ActionResult process(PipelineContext<String> context) {
        String fileName = context.getInputParams()[0];

        PipeSystem<String> graph = context.getSystem();

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
