package microservice_traceability.traceability.domain.exception;

public class OrderStatusHistoryNotFoundException extends RuntimeException {
    public OrderStatusHistoryNotFoundException(String message) {
        super(message);
    }
}
