package ford;

/**
 * Enum representing the reason for failure of FF algorithm
 */
public enum FordFulkersonError {
    UNKNOWN("An unknown error occured"),
    MISSING_VERTEX("Source or destination point does not exist"),
    SRC_AND_DEST_IDENTICAL("The source & desination points cannot be identical"),
    NO_PATH_EXIST("A path between the source & desination points could not be found");

    String message;
    FordFulkersonError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
