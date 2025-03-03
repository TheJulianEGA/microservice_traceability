package microservice_traceability.traceability.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import microservice_traceability.traceability.infrastructure.security.service.jwtdetail.JwtDetailsService;
import microservice_traceability.traceability.infrastructure.util.InfrastructureConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtDetailsService jwtDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(InfrastructureConstants.AUTH_HEADER);

        if (authHeader == null || !authHeader.startsWith(InfrastructureConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(InfrastructureConstants.TOKEN_PREFIX_LENGTH);

        UserDetails user = jwtDetailsService.loadUserByUsername(jwt);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, jwt, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
