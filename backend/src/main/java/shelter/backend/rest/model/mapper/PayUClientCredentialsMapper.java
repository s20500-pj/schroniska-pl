package shelter.backend.rest.model.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import shelter.backend.rest.model.dtos.PayUClientCredentialsDto;
import shelter.backend.rest.model.entity.PayUClientCredentials;
import shelter.backend.rest.model.entity.User;
import shelter.backend.storage.repository.PayUClientCredentialsRepository;
import shelter.backend.storage.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PayUClientCredentialsMapper implements DtoEntityMapper<PayUClientCredentials, PayUClientCredentialsDto> {

    private final PayUClientCredentialsRepository payUClientCredentialsRepository;

    private final UserRepository userRepository;

    @Override
    public PayUClientCredentials toEntity(PayUClientCredentialsDto payUClientCredentialsDto) {
        User shelter = userRepository.findById(payUClientCredentialsDto.getShelterId())
                .orElseThrow(() -> new EntityNotFoundException("Nie istnieje schronisko dla wybranego id: " + payUClientCredentialsDto.getShelterId()));
        return Optional.ofNullable(payUClientCredentialsDto.getId())
                .flatMap(payUClientCredentialsRepository::findById)
                .map(entity -> entity.toEntity(payUClientCredentialsDto, shelter))
                .orElseGet(() -> new PayUClientCredentials().toEntity(payUClientCredentialsDto, shelter));
    }

    @Override
    public PayUClientCredentialsDto toDto(PayUClientCredentials entity) {
        throw new NotImplementedException();
    }

}
