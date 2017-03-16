import action.UserAction;
import action.WildcardArgsAction;
import action.impl.*;
import action.result.ActionResult;
import data.PipelineContext;

import java.util.*;

public class Pipeline {
    private static Scanner userScanner = new Scanner(System.in);

    private static final Map<Integer, UserAction> USER_ACTIONS = new HashMap<>();

    static {
        USER_ACTIONS.put(1, new LoadGraphAction());
        USER_ACTIONS.put(2, new AddPipeAction());
        USER_ACTIONS.put(3, new RemovePipeAction());
        USER_ACTIONS.put(4, new UndoRemoveAction());
        USER_ACTIONS.put(5, new AdjacencyListAction());
        USER_ACTIONS.put(6, new TraverseGraphAction());
        USER_ACTIONS.put(7, new SolveGraphAction());
        USER_ACTIONS.put(8, new WriteSolutionAction());
        USER_ACTIONS.put(9, new SaveGraphAction());
    }

    public static void main(String[] args) {
        printWelcome();

        PipelineContext<String> context = new PipelineContext<>(null, null);
        processCommandLoop(context);
    }

    private static void processCommandLoop(PipelineContext<String> context) {
        mainLoop:
        while (true) {

            System.out.println();
            System.out.print("> Select command: ");
            String input = userScanner.nextLine();

            try {
                String[] parts = input.split(" ", 2);
                String payload = parts[0];

                if (payload.toLowerCase().startsWith("q")) {
                    System.out.println("Bye bye!");
                    return;
                }

                int command = Integer.parseInt(payload);

                UserAction userAction = USER_ACTIONS.get(command);

                if (userAction == null) {
                    System.out.println("Unrecognized command...");
                    printHelp();
                    continue;
                }

                if (userAction.requiresGraphLoaded() && context.getSystem() == null) {
                    System.out.println("You have not loaded a graph!");
                    continue;
                }

                ActionResult result = null;

                do {
                    if (result != null) {
                        System.out.println("Error: " + result.getMessage());
                    }

                    if (userAction.requiresArgs() && userAction instanceof WildcardArgsAction) {

                        String[] params = queryParams((WildcardArgsAction) userAction);
                        if (params == null) {
                            continue mainLoop;
                        }

                        context.setInputParams(params);
                        System.out.println();
                    }
                }
                while (!((result = userAction.act(context)).isSuccess()) && userAction instanceof WildcardArgsAction);

                if (!result.isSuccess()) {
                    System.out.println("Error: " + result.getMessage());
                } else if (result.getMessage() != null) {
                    System.out.println("Success: " + result.getMessage());
                }

            } catch (NumberFormatException ex) {
                System.out.println("Unrecognized number...");
                printHelp();
            }
        }
    }

    private static String[] queryParams(WildcardArgsAction userAction) {
        String[] args = new String[userAction.getParameterNames().length];

        for (int i = 0; i < args.length; i++) {
            System.out.print(userAction.getName() + " :> Enter " + userAction.getParameterNames()[i] + " (Leave blank to cancel): ");

            String input = userScanner.nextLine();
            if (input == null || input.isEmpty()) {
                return null;
            }
            args[i] = input;
        }

        return args;
    }

    private static void printWelcome() {
        System.out.println();
        System.out.println(String.format("%50s", "Welcome to Pipeline!"));
        System.out.println();

        printHelp();

    }

    private static final int NUM_WIDTH = 2;
    private static final int DESC_WIDTH = 37;
    private static final int PARAMS_WIDTH = 32;
    private static final int HELP_TABLE_WIDTH = PARAMS_WIDTH + DESC_WIDTH + NUM_WIDTH + 5;

    private static void printHelp() {
        String separator = "+" + String.join("", Collections.nCopies(HELP_TABLE_WIDTH - 2, "-")) + "+";

        System.out.println();

        System.out.println(separator);
        System.out.println(String.format("| %" + DESC_WIDTH + "s%" + (HELP_TABLE_WIDTH - (DESC_WIDTH + 2)) + "s ", "AVAILABLE COMMANDS: ", "|"));

        System.out.println(separator);
        System.out.println(String.format("| %-" + NUM_WIDTH + "s %-" + DESC_WIDTH + "s %-" + PARAMS_WIDTH + "s|", "#", "Description", "Parameters (If applicable)"));
        System.out.println(separator);

        for (Map.Entry<Integer, UserAction> actions : USER_ACTIONS.entrySet()) {
            int i = actions.getKey();
            UserAction action = actions.getValue();
            System.out.println("| " + String.format("%-" + NUM_WIDTH + "s %-" + DESC_WIDTH + "s %-" + PARAMS_WIDTH + "s", i + ".", action.getDescription(),
                    action instanceof WildcardArgsAction ? Arrays.asList(((WildcardArgsAction) action).getParameterNames()) : "") + "|");
        }
        System.out.println(separator);
    }

}
