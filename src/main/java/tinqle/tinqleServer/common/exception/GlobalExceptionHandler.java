package tinqle.tinqleServer.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
}
