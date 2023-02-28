package shelter.backend.login.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shelter.backend.login.JwtUtils;
import shelter.backend.utils.exception.AuthenticationException;
import shelter.backend.rest.model.dtos.AuthenticationRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterAuthenticationService implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public String authenticate(AuthenticationRequestDto request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            log.info("Authentication successful for: {}", userDetails.getUsername());
            return jwtUtils.generateToken(userDetails);
        } catch (Exception e) {
            log.error("Unable to authenticate the user: {}, exception message: {}", request.getEmail(), e.getMessage());
            throw new AuthenticationException("Failed to authenticate user", e);
        }
    }
}
