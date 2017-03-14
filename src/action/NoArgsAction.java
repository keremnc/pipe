package action;

public abstract class NoArgsAction implements UserAction {
    @Override
    public final boolean requiresArgs() {
        return false;
    }
}
