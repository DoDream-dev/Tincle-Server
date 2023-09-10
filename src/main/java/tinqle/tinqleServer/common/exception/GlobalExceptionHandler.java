package tinqle.tinqleServer.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.domain.account.exception.AuthException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TinqleException.class)
    public ResponseEntity<?> handle(TinqleException ex) {
        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), ex.statusCode.getStatusCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.statusCode.getHttpCode())
                .body(ApiResponse.error(ex.statusCode.getStatusCode(), ex.statusCode.getMessage()));
    }

    @ExceptionHandler(AuthException.class)  //분리 이유: SignToken
    public ResponseEntity<?> handle(AuthException ex) {
        log.warn("{}({}) - {}", ex.getClass().getSimpleName(), ex.statusCode.getStatusCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.statusCode.getHttpCode())
                .body(ApiResponse.error(ex.statusCode.getStatusCode(), ex.data, ex.statusCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleInternalError(final Exception ex) {
        log.error("Uncaught {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(StatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), ex.getMessage()));
    }
}
