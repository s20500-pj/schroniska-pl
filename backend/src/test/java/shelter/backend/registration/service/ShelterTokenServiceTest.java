package shelter.backend.registration.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import shelter.backend.rest.model.entity.Token;
import shelter.backend.storage.repository.TokenRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterTokenServiceTest {

    @InjectMocks
    ShelterTokenService tokenService;

    @Mock
    private TokenRepository tokenRepository;

    private Token token;

    @BeforeEach
    public void setUp(){

        ReflectionTestUtils.setField(tokenService, "expirationTime", 15);

        int hashCode = new Random().hashCode();
        String tokenValue = UUID.randomUUID().toString();
        String id = hashCode + ";" + tokenValue;
        token = new Token(id, tokenValue, "userName", new Date());
    }

    @Test
    void getToken() {
        //
        when(tokenRepository.findById(token.getId())).thenReturn(Optional.of(token));
        //
        Token result = tokenService.getToken(token.getId());
        //
        Assertions.assertEquals(result, token);

    }

    @Test
    void generateToken() {
        //
        //
        Token result = tokenService.generateToken(new Random().hashCode(), "userName");
        //
        Assertions.assertEquals(result.getUsername(), "userName");
        Assertions.assertEquals(getDay(result.getCreatedAt()), getDay(new Date()));
        verify(tokenRepository, times(1)).save(any());
    }

    private int getDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Test
    void isExpired() {
        //
        //
        boolean result = tokenService.isExpired(token);
        //
        Assertions.assertFalse(result);
    }

    @Test
    void deleteToken() {
        //
        //
        tokenService.deleteToken(token);
        //
        verify(tokenRepository, times(1)).delete(token);
    }

    @Test
    void findAll() {
        //
        when(tokenRepository.findAll()).thenReturn(List.of(token, new Token("id", "token", "userName2", new Date())));
        //
        List<Token> result = (List<Token>) tokenService.findAll();
        //
        Assertions.assertFalse(result.isEmpty());
    }
}


