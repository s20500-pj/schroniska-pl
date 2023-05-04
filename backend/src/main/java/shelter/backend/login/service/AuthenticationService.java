package shelter.backend.login.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shelter.backend.login.rest.dtos.AuthenticationResponseDto;
import shelter.backend.rest.model.dtos.AuthenticationRequestDto;

public interface AuthenticationService {
    AuthenticationResponseDto authenticate (AuthenticationRequestDto requestDto);
    void clearCookies(HttpServletRequest request, HttpServletResponse response);

}

