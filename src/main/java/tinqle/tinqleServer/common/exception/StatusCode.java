package tinqle.tinqleServer.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /**
     * User
     */

    // fail
    FILTER_ACCESS_DENIED(401, 1000, "access denied.");

    private final int HttpCode;
    private final int statusCode;
    private final String message;
}
