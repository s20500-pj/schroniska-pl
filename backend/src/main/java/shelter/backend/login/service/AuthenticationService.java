package shelter.backend.login.service;

import shelter.backend.rest.model.dtos.AuthenticationRequestDto;

public interface AuthenticationService {
    String authenticate (AuthenticationRequestDto requestDto);
}
