package microservice_traceability.traceability.infrastructure.exception.global;

import microservice_traceability.traceability.domain.exception.NoAuthenticatedUserIdFoundException;
import microservice_traceability.traceability.domain.exception.OrderAccessForbiddenException;
import microservice_traceability.traceability.domain.exception.OrderStatusHistoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderStatusHistoryNotFoundException.class)
    public ResponseEntity<String> orderStatusHistoryNotFoundException(OrderStatusHistoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(OrderAccessForbiddenException.class)
    public ResponseEntity<String> orderAccessForbiddenException(OrderAccessForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NoAuthenticatedUserIdFoundException.class)
    public ResponseEntity<String> noAuthenticatedUserIdFoundException(NoAuthenticatedUserIdFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
