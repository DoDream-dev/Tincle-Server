package tinqle.tinqleServer.domain.account.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class AccountException extends TinqleException {
    public AccountException(StatusCode statusCode) {
        super(statusCode);
    }

    public AccountException(StatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
