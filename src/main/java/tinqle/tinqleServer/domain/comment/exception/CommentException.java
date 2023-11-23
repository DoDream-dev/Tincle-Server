package tinqle.tinqleServer.domain.comment.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class CommentException extends TinqleException {
    public CommentException(StatusCode statusCode) {
        super(statusCode);
    }
}
