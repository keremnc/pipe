package action.impl;

import action.WildcardArgsAction;
import action.result.ActionResult;
import data.PipelineContext;
import ford.FordFulkerson;
import struct.FlowGraph;

import java.io.PrintWriter;

public class SolveGraphAction extends WildcardArgsAction {
    @Override
    public boolean requiresGraphLoaded() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Calculate & display max flow network";
    }

    @Override
    public String getName() {
        return "Solve graph";
    }

    @Override
    public ActionResult process(PipelineContext<String> context) {
        FlowGraph<String> graph = context.getSystem();

        String srcStr = context.getInputParams()[0].toUpperCase();
        String dstStr = context.getInputParams()[1].toUpperCase();

        if (!graph.contains(srcStr) || !graph.contains(dstStr)) {
            return new ActionResult(false, "Source or destination does not exist!");
        }

        if (graph.isSolved()) {
            System.out.println("Existing graph is already solved... Displaying cached value.");
        } else {
            FordFulkerson.findMaxFlow(graph, srcStr, dstStr);

            if (!graph.isSolved()) {
                return new ActionResult(false, "Unable to solve graph!");
            }
        }

        graph.printWithFlows(new PrintWriter(System.out));
        System.out.println("Max flow: " + graph.getMaxFlow());

        return new ActionResult(true, null);
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"source", "sink"};
    }
}
