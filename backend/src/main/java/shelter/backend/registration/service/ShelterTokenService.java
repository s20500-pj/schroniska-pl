package shelter.backend.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shelter.backend.rest.model.entity.Token;
import shelter.backend.storage.repository.TokenRepository;
import shelter.backend.utils.exception.TokenNotFound;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShelterTokenService implements TokenService {
    @Value("${shelter.redis.token.expiration.minutes}")
    private int expirationTime;

    private final TokenRepository tokenRepository;

    @Override
    public Token getToken(String tokenId) {
        return tokenRepository.findById(tokenId).orElseThrow(() -> new TokenNotFound("Token Not Found"));
    }

    @Override
    public Token generateToken(int hashcode, String username) {
        String tokenValue = UUID.randomUUID().toString();
        String id = hashcode + ";" + tokenValue;
        Token token = new Token(id, tokenValue, username, new Date());
        tokenRepository.save(token);
        log.info("Token {} saved for username: {}", tokenValue, username);
        return token;
    }

    @Override
    public boolean isExpired(Token token) {
        Date current = new Date();
        Date tokenTTL = DateUtils.addMinutes(token.getCreatedAt(), expirationTime);
        return tokenTTL.before(current);
    }

    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }

    public Iterable<Token> findAll() {
        return tokenRepository.findAll();
    }

}
