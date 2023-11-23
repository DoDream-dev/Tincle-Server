package tinqle.tinqleServer.util.image.exception;

import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.common.exception.TinqleException;

public class ImageException extends TinqleException {
    public ImageException(StatusCode statusCode) {
        super(statusCode);
    }

}
