package tinqle.tinqleServer.common.exception;

public class TinqleException extends RuntimeException {
    public StatusCode statusCode;
    public TinqleException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode=statusCode;
    }

    public TinqleException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
