package action.impl;

import action.WildcardArgsAction;
import action.result.ActionResult;
import struct.FlowGraph;
import data.PipelineContext;

public class AddPipeAction extends WildcardArgsAction {

    @Override
    public ActionResult process(PipelineContext<String> context) {
        FlowGraph<String> graph = context.getSystem();

        String srcStr = context.getInputParams()[0].toUpperCase();
        String dstStr = context.getInputParams()[1].toUpperCase();

        if (srcStr.contains("\\|") || dstStr.contains("\\|") || srcStr.contains(",") || dstStr.contains(",")) {
            return new ActionResult(false, "Illegal character! Cannot use '|' or ',' symbol");
        }

        String capStr = context.getInputParams()[2];

        double capacity;

        try {
            capacity = Double.parseDouble(capStr);
        } catch (NumberFormatException ex) {
            return new ActionResult(false, "Unable to parse '" + capStr + "' to Double");
        }
        context.getSystem().addEdge(srcStr, dstStr, capacity);

        return new ActionResult(true, String.format("Pipe {%s->%s (%s)} added!", srcStr, dstStr, capStr));

    }

    @Override
    public boolean requiresGraphLoaded() {
        return true;
    }

    @Override
    public String getName() {
        return "Add pipe";
    }

    @Override
    public String getDescription() {
        return "Add a pipe to the graph";
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"source", "destination", "capacity"};
    }
}
