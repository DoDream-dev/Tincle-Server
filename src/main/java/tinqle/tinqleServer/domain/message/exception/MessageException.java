package tinqle.tinqleServer.domain.message.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class MessageException extends TinqleException {
    public MessageException(StatusCode statusCode) {
        super(statusCode);
    }
}
