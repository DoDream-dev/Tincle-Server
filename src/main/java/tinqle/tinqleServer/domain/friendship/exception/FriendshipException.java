package tinqle.tinqleServer.domain.friendship.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class FriendshipException extends TinqleException {
    public FriendshipException(StatusCode statusCode) {
        super(statusCode);
    }
}
