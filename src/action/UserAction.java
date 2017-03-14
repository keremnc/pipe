package action;

import action.result.ActionResult;
import data.PipelineContext;

public interface UserAction {
    ActionResult act(PipelineContext<String> context);

    boolean requiresArgs();

    boolean requiresGraphLoaded();

    String getDescription();

    String getName();

}
