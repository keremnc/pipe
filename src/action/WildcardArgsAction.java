package action;

import action.result.ActionResult;
import io.PipelineContext;

public abstract class WildcardArgsAction implements UserAction {
    private static final ActionResult MISSING_PARAM_RESULT = new ActionResult(false, "Please provide argument");

    public abstract ActionResult process(PipelineContext<String> context);
    public abstract String[] getParameterNames();

    @Override
    public boolean requiresArgs() {
        return true;
    }

    @Override
    public final ActionResult act(PipelineContext<String> context) {
        if (context.getInputParams() == null || context.getInputParams().length == 0) {
            return MISSING_PARAM_RESULT;
        }
        return process(context);
    }

}
