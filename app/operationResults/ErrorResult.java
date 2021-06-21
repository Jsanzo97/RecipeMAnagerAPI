package operationResults;

public class ErrorResult extends OperationResult {

    private final ErrorType errorType;

    public ErrorResult(ErrorType errorType, String description) {
        super(errorType.getCode(), description);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
