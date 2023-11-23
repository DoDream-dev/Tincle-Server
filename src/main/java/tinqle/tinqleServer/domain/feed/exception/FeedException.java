package tinqle.tinqleServer.domain.feed.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class FeedException extends TinqleException {
    public FeedException(StatusCode statusCode) {
        super(statusCode);
    }
}
