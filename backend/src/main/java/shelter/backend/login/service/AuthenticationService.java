package shelter.backend.login.service;

import shelter.backend.login.rest.dtos.AuthenticationResponseDto;
import shelter.backend.rest.model.dtos.AuthenticationRequestDto;

public interface AuthenticationService {
    AuthenticationResponseDto authenticate (AuthenticationRequestDto requestDto);
}
