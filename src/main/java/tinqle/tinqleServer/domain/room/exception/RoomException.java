package tinqle.tinqleServer.domain.room.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class RoomException extends TinqleException {
    public RoomException(StatusCode statusCode) {
        super(statusCode);
    }
}
