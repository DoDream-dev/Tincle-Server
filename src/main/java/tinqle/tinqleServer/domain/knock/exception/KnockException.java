package tinqle.tinqleServer.domain.knock.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class KnockException extends TinqleException {
    public KnockException(StatusCode statusCode) {
        super(statusCode);
    }
}
