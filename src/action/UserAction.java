package action;

import action.result.ActionResult;
import io.PipelineContext;

public interface UserAction {
    ActionResult act(PipelineContext<String> context);

    boolean requiresArgs();

    boolean requiresGraphLoaded();

    String getDescription();

    String getName();

}
