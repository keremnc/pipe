package action.impl;

import action.UserAction;
import action.ActionResult;
import data.PipelineContext;
import ford.FordFulkerson;
import struct.FlowGraph;
import struct.Pair;

import java.io.PrintWriter;

/**
 * Action to solve & display max-flow for {@link FlowGraph}
 */
public class SolveGraphAction implements UserAction {
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
    public ActionResult act(PipelineContext<String> context) {
        FlowGraph<String> graph = context.getSystem();

        String srcStr = context.getInputParams()[0];
        String dstStr = context.getInputParams()[1];

        Pair<String, String> solvedVertices = graph.getSolvedVertices();
        if (graph.isSolved() && solvedVertices != null && solvedVertices.first.equals(srcStr) && solvedVertices.second.equals(dstStr)) {
            System.out.println("Existing graph is already solved... Displaying cached value.");
        } else {
            FordFulkerson.findMaxFlow(graph, srcStr, dstStr);

            if (!graph.isSolved()) {
                return new ActionResult(false, "Unable to solve graph - " + graph.getSolutionError().getMessage() + "");
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
