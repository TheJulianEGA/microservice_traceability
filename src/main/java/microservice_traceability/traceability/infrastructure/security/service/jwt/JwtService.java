package microservice_traceability.traceability.infrastructure.security.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.infrastructure.util.InfrastructureConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    @Override
    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    @Override
    public String extractRole(String jwt) {
        return extractAllClaims(jwt).get(InfrastructureConstants.CLAIM_AUTHORITIES).toString();
    }

    private Key generateKey() {
        byte[] secretAsBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretAsBytes);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder().setSigningKey(generateKey()).build()
                .parseClaimsJws(jwt).getBody();
    }
}