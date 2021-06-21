package operationResults;

public class OkResult extends OperationResult {

    private final static int code = 200;

    public OkResult(String description) {
        super(code, description);
    }

}
