package tinqle.tinqleServer.domain.emoticon.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class EmoticonException extends TinqleException {
    public EmoticonException(StatusCode statusCode) {
        super(statusCode);
    }
}
