package tinqle.tinqleServer.domain.account.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class AuthException extends TinqleException {
    public Object data;

    public AuthException(StatusCode statusCode) {
        super(statusCode);
    }

    public AuthException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }

    public AuthException(StatusCode statusCode, Object data) {
        super(statusCode);
        this.data = data;
    }
}
