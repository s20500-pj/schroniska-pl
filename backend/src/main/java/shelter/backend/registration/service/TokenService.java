package shelter.backend.registration.service;

import shelter.backend.rest.model.entity.Token;

public interface TokenService {

    Token getToken(String tokenId);

    Token generateToken(int hashcode, String username);

    boolean isExpired(Token token);

    void deleteToken(Token token);

    Iterable<Token> findAll();
}
