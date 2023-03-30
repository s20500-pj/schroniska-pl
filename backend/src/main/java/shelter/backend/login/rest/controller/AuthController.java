package shelter.backend.login.rest.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import shelter.backend.configuration.security.CookieAuthenticationFilter;
import shelter.backend.login.rest.dtos.AuthenticationResponseDto;
import shelter.backend.login.service.AuthenticationService;
import shelter.backend.rest.model.dtos.AuthenticationRequestDto;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> signIn(@RequestBody AuthenticationRequestDto request,
                                                            HttpServletResponse servletResponse) {
        AuthenticationResponseDto responseDto = authService.authenticate(request);
        Cookie authCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, responseDto.getAuthToken());
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
        authCookie.setPath("/");

        servletResponse.addCookie(authCookie);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        return ResponseEntity.noContent().build();
    }
}
