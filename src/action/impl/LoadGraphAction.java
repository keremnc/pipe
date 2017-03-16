package action.impl;

import action.FileIOGraphAction;
import action.result.ActionResult;
import struct.FlowGraph;
import data.PipelineContext;

import java.util.Scanner;

public class LoadGraphAction extends FileIOGraphAction {
    public static final String MAGIC_DELETION_DELIMITER = ",";

    @Override
    public ActionResult handleFile(PipelineContext<String> context, Scanner fileScanner) {

        FlowGraph<String> graph = new FlowGraph<>();

        try {
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();

                if (line.startsWith(MAGIC_DELETION_DELIMITER)) { // Read previous deletions from this line

                    String deletionData = line.split(MAGIC_DELETION_DELIMITER, 2)[1];

                    if (deletionData != null && !deletionData.isEmpty()) {
                        String[] deletionParts = deletionData.split(",");

                        for (int i = deletionParts.length - 1; i >= 0; i--) { // Reverse order since stack is LIFO
                            String deletedPart = deletionParts[i];
                            String[] comp = deletedPart.split("\\|");

                            String src = comp[0];
                            String dst = comp[1];
                            double cost = Double.parseDouble(comp[2]);
                            long millis = Long.parseLong(comp[3]);

                            graph.registerDeletion(src, dst, cost, millis);
                        }

                        continue;
                    }
                }
                String[] parts = line.split("\\|", 2);

                String vertex = parts[0].trim();

                if (parts.length > 1 && !parts[1].isEmpty()) {
                    String[] adjacencyList = parts[1].trim().split(",");

                    for (String adj : adjacencyList) {
                        String[] payload = adj.trim().split("\\|");

                        String adjVertex = payload[0].trim();
                        double capacity = Double.parseDouble(payload[1].trim());

                        graph.addEdge(vertex, adjVertex, capacity);
                        System.out.println(String.format("Pipe {%s->%s (%s)} added!", vertex, adjVertex, capacity));
                    }
                }
            }
        } catch (NumberFormatException ex) {
            return new ActionResult(false, "Unable to load graph: " + ex.getMessage());

        }

        context.setSystem(graph);

        return new ActionResult(true, "Successfully loaded graph!");

    }

    @Override
    public boolean requiresGraphLoaded() {
        return false;
    }

    @Override
    public String getName() {
        return "Load graph";
    }

    @Override
    public String getDescription() {
        return "Load graph from a file";
    }

}
