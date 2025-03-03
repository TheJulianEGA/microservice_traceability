package microservice_traceability.traceability.infrastructure.security.service.jwt;

public interface IJwtService {

    String extractUsername(String jwt);

    String extractRole(String jwt);

}
