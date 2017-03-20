import action.UserAction;
import action.impl.*;
import action.ActionResult;
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

    /**
     * Main loop for command processing
     *
     * @param context data container object
     */
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

                    if (userAction.getParameterNames() != null) {

                        String[] params = queryParams(userAction);
                        if (params == null) { // If a arg is empty, assume the user wants to return to main menu
                            continue mainLoop;
                        }

                        context.setInputParams(params);
                        System.out.println();
                    }
                }
                while (!((result = userAction.act(context)).isSuccess()) && userAction.getParameterNames() != null);
                // We should retry the same command only if the command requires arguments
                // This is because a no-arg command will likely fail over and over again regardless of the circumstances
                // However, a command requiring args has likely failed due to malformed argument input

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

    /**
     * Blocks and returns an array of argument inputs corresponding to given {@link UserAction} parameter length
     *
     * @param userAction {@link UserAction} with not-null parameters
     * @return string array with same length of UserAction.getParameterNames().length
     */
    private static String[] queryParams(UserAction userAction) {
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

    /**
     * Prints the initial message that should be displayed when a user first launches the program
     */
    private static void printWelcome() {
        System.out.println();
        System.out.println(String.format("%50s", "Welcome to Pipeline!"));
        System.out.println();

        printHelp();

    }

    private static final int NUM_WIDTH = 2; // width of command key column
    private static final int DESC_WIDTH = 37; // width of description column
    private static final int PARAMS_WIDTH = 32; // width of param info column
    private static final int HELP_TABLE_WIDTH = PARAMS_WIDTH + DESC_WIDTH + NUM_WIDTH + 5;

    /**
     * Prints a table of command infos
     */
    private static void printHelp() {
        String separator = "+" + String.join("", Collections.nCopies(HELP_TABLE_WIDTH - 2, "-")) + "+";
        // creates a separator between table partitions

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
                    action.getParameterNames() == null ? "" : Arrays.asList(action.getParameterNames())) + "|");
        }
        System.out.println(separator);
        System.out.println(String.format("| %" + DESC_WIDTH + "s%" + (HELP_TABLE_WIDTH - (DESC_WIDTH + 2)) + "s ", "Type 'q' to quit ", "|"));
        System.out.println(separator);

    }

}
