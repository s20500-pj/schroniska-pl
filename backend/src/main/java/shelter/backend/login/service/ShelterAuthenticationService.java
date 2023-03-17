package shelter.backend.login.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shelter.backend.login.JwtUtils;
import shelter.backend.login.rest.dtos.AuthenticationResponseDto;
import shelter.backend.rest.model.entity.Role;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.exception.AuthenticationException;
import shelter.backend.rest.model.dtos.AuthenticationRequestDto;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterAuthenticationService implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            log.info("Authentication successful for: {}", userDetails.getUsername());
            String token = jwtUtils.generateToken(userDetails);
            User user = userRepository.findUserByEmail(request.getEmail());
            return buildAuthenticationResponseDto(user.getId(), token, user.getEmail(),
                    user.getFirstName(), user.getLastName(), user.getShelterName(), user.getUserType(), user.getRoles());
        } catch (Exception e) {
            log.error("Unable to authenticate the user: {}, exception message: {}", request.getEmail(), e.getMessage());
            throw new AuthenticationException("Failed to authenticate user", e);
        }
    }

    private AuthenticationResponseDto buildAuthenticationResponseDto(Long userId, String authToken, String firstName,
                                                                     String lastName, String userEmail, String shelterName,
                                                                     UserType userType, Set<Role> roles) {
        return AuthenticationResponseDto.builder()
                .userId(userId)
                .userEmail(userEmail)
                .firstName(firstName)
                .lastName(lastName)
                .shelterName(shelterName)
                .userType(userType)
                .authToken(authToken)
                .roles(roles)
                .build();
    }
}
