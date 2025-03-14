package microservice_traceability.traceability.infrastructure.security.adapter;

import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.domain.exception.NoAuthenticatedUserIdFoundException;
import microservice_traceability.traceability.domain.security.IAuthenticationSecurityPort;
import microservice_traceability.traceability.infrastructure.util.InfrastructureConstants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationAdapter implements IAuthenticationSecurityPort {

    @Override
    public Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return parseUserId(userDetails.getUsername());
        } else if (principal instanceof String userId) {
            return parseUserId(userId);
        } else {
            throw new NoAuthenticatedUserIdFoundException(InfrastructureConstants.ERROR_CONVERTING_USER_ID);
        }
    }

    private Long parseUserId(String userId) {
        try {
            return Long.valueOf(userId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Error al convertir el ID de usuario: " + userId);
        }
    }
}
