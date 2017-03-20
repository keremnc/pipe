package action;

import data.PipelineContext;

/**
 * Represents an action a user can enact upon a {@link PipelineContext} object.
 */
public interface UserAction {

    /**
     * Runs the action on the {@link PipelineContext}
     *
     * @param context current data context
     * @return result of action
     */
    ActionResult act(PipelineContext<String> context);

    /**
     * List of parameters the command expects & assumes the user will pass
     *
     * @return parameter names
     */
    default String[] getParameterNames() {
        return null;
    }

    /**
     * Whether the action requires a graph to have been loaded
     *
     * @return boolean
     */
    default boolean requiresGraphLoaded() {
        return false;
    }

    /**
     * Longer description of action
     *
     * @return description
     */
    String getDescription();

    /**
     * Short name for action
     *
     * @return name
     */
    String getName();

}
