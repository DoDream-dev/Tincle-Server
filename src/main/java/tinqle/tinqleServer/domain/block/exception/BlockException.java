package tinqle.tinqleServer.domain.block.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class BlockException extends TinqleException {
    public BlockException(StatusCode statusCode) {
        super(statusCode);
    }
}
