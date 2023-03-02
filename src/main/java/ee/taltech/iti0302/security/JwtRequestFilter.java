package ee.taltech.iti0302.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        Optional<String> jwt = getToken(request);
        if (jwt.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }
        Claims tokenBody = parseToken(jwt.get());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(buildAuthToken(tokenBody));

        chain.doFilter(request, response);
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(JwtTokenProvider.key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Optional<String> getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return Optional.empty();
        }
        log.info("Getting token");
        return Optional.of(header.substring("Bearer ".length()));
    }

    private UsernamePasswordAuthenticationToken buildAuthToken(Claims claims) {
        log.info("Building authentication token");
        return new UsernamePasswordAuthenticationToken(claims.get("email"), // Set info (can be an DTO) of logged in user
                "",
                List.of(new SimpleGrantedAuthority("USER"))); // List of roles
    }
}
