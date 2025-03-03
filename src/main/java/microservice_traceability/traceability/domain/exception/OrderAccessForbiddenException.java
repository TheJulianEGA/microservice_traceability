package microservice_traceability.traceability.domain.exception;

public class OrderAccessForbiddenException extends RuntimeException {
    public OrderAccessForbiddenException(String message) {
        super(message);
    }
}
