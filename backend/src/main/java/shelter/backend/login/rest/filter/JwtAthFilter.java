package shelter.backend.login.rest.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shelter.backend.login.JwtUtils;
import shelter.backend.login.service.UserDetailsService;
import shelter.backend.utils.constants.ShelterConstants;
import shelter.backend.utils.exception.AdoptionException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String jwt;
        String userEmail = null;

        Optional<Cookie> cookie = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(c -> c.getName().equals(ShelterConstants.AUTHORIZATION_COOKIE_NAME))
                .findFirst();

        if (cookie.isPresent()) {
            jwt = cookie.get().getValue();
            try {
                userEmail = jwtUtils.extractUsername(jwt);
            } catch (IllegalArgumentException e) {
                log.info("Unable to get JWT Token. Message: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                log.info("JWT Token has expired for username: {}", e.getClaims().getSubject());
                throw new AdoptionException("Token nieważny. Zaloguj się ponownie");
            }
            setAuthentication(request, jwt, userEmail);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, String jwt, String userEmail) {
        if (userEmail != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtUtils.isTokenValid(jwt, userDetails)) {
                log.debug("JWT Valid for: {}", userDetails.getUsername());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }
}
