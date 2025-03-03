package microservice_traceability.traceability.infrastructure.security.service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import microservice_traceability.traceability.infrastructure.util.InfrastructureConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private String secretKey = "MySuperSecretKeyForJwtMySuperSecretKeyForJwt";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
    }

    @Test
    void extractUsername_ValidJwt_ReturnsUsername() {

        String expectedUsername = "user123";
        String jwt = generateTestToken(expectedUsername, "ROLE_USER");

        String extractedUsername = jwtService.extractUsername(jwt);

        assertEquals(expectedUsername, extractedUsername);
    }

    @Test
    void extractRole_ValidJwt_ReturnsRole() {

        String expectedRole = "ROLE_ADMIN";
        String jwt = generateTestToken("user123", expectedRole);

        String extractedRole = jwtService.extractRole(jwt);

        assertEquals(expectedRole, extractedRole);
    }

    @Test
    void extractUsername_InvalidJwt_ThrowsException() {

        String invalidJwt = "invalid.jwt.token";

        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidJwt));
    }

    private String generateTestToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(InfrastructureConstants.CLAIM_AUTHORITIES, role);

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }
}
