package shelter.backend.login.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shelter.backend.login.rest.dtos.AuthenticationResponseDto;
import shelter.backend.login.service.AuthenticationService;
import shelter.backend.rest.model.dtos.AuthenticationRequestDto;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
