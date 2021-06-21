package operationResults;

public abstract class OperationResult {

    protected int code;
    protected String description;

    public OperationResult(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
