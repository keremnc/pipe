package action.impl;

import action.UserAction;
import action.ActionResult;
import data.PipelineContext;
import struct.FlowGraph;

/**
 * Action to add an edge to the {@link FlowGraph}
 */
public class AddPipeAction implements UserAction {

    @Override
    public ActionResult act(PipelineContext<String> context) {
        FlowGraph<String> graph = context.getSystem();

        String srcStr = context.getInputParams()[0];
        String dstStr = context.getInputParams()[1];

        // These are characters we use for serializing the graph data
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
        graph.addEdge(srcStr, dstStr, capacity);

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
