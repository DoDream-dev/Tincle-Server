package tinqle.tinqleServer.domain.notification.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class NotificationException extends TinqleException {
    public NotificationException(StatusCode statusCode) {
        super(statusCode);
    }
}
