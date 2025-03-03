package microservice_traceability.traceability.infrastructure.security.service.jwtdetail;

import microservice_traceability.traceability.infrastructure.security.service.jwt.IJwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtDetailsServiceTest {

    @Mock
    private IJwtService jwtService;

    @InjectMocks
    private JwtDetailsService jwtDetailsService;

    @Test
    void loadUserByUsername_ValidJwt_ReturnsUserDetails() {

        String jwt = "valid.jwt.token";
        String expectedUsername = "user123";
        String expectedRole = "ROLE_USER";

        when(jwtService.extractUsername(jwt)).thenReturn(expectedUsername);
        when(jwtService.extractRole(jwt)).thenReturn(expectedRole);

        UserDetails userDetails = jwtDetailsService.loadUserByUsername(jwt);

        assertNotNull(userDetails);
        assertEquals(expectedUsername, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(expectedRole)));

       verify(jwtService, times(1)).extractUsername(jwt);
       verify(jwtService, times(1)).extractRole(jwt);
    }

    @Test
    void loadUserByUsername_InvalidJwt_ThrowsException() {

        String invalidJwt = "invalid.jwt.token";

        when(jwtService.extractUsername(invalidJwt)).thenThrow(new UsernameNotFoundException("Invalid JWT"));

        assertThrows(UsernameNotFoundException.class, () -> jwtDetailsService.loadUserByUsername(invalidJwt));
    }
}