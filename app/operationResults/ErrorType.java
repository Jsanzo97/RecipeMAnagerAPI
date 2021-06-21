package operationResults;

public enum ErrorType {
    USER_WITH_USERNAME_ALREADY_EXISTS(409),
    RECIPE_WITH_NAME_ALREADY_EXISTS(409),
    USER_WITH_USERNAME_NO_EXISTS(400),
    WRONG_PASSWORD(400),
    EMPTY_BODY(400),
    PARAMETERS_ERROR(400),
    RECIPE_WITH_NAME_NO_EXISTS(400);

    private final int code;

    ErrorType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}