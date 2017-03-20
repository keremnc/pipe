package action;

/**
 * Data object representing the state of a {@link UserAction}
 */
public class ActionResult {

    private boolean success;
    private String message;

    public ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
