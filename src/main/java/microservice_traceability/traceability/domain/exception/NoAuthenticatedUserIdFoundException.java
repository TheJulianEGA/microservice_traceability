package microservice_traceability.traceability.domain.exception;

public class NoAuthenticatedUserIdFoundException extends RuntimeException {
    public NoAuthenticatedUserIdFoundException(String message) {
        super(message);
    }
}
