package microservice_traceability.traceability.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import microservice_traceability.traceability.infrastructure.security.service.jwtdetail.JwtDetailsService;
import microservice_traceability.traceability.infrastructure.util.InfrastructureConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtDetailsService jwtDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private final String jwtToken = "valid.jwt.token";
    private final String authHeader = "Bearer " + jwtToken;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_ValidToken_SetsAuthentication() throws ServletException, IOException {

        UserDetails userDetails = new User("user123", "", List.of());
        when(request.getHeader(InfrastructureConstants.AUTH_HEADER)).thenReturn(authHeader);
        when(jwtDetailsService.loadUserByUsername(jwtToken)).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user123", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtDetailsService, times(1)).loadUserByUsername(jwtToken);
    }

    @Test
    void doFilterInternal_NoAuthHeader_DoesNotSetAuthentication() throws ServletException, IOException {

        when(request.getHeader(InfrastructureConstants.AUTH_HEADER)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidAuthHeader_DoesNotSetAuthentication() throws ServletException, IOException {

        when(request.getHeader(InfrastructureConstants.AUTH_HEADER)).thenReturn("InvalidToken");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
