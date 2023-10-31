package tinqle.tinqleServer.domain.messageBox.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class MessageBoxException extends TinqleException {
    public MessageBoxException(StatusCode statusCode) {
        super(statusCode);
    }
}
