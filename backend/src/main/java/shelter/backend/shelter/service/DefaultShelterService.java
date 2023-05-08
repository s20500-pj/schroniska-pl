package shelter.backend.shelter.service;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.UserType;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.rest.model.specification.UserSpecification;
import shelter.backend.storage.repository.UserRepository;
import shelter.backend.utils.basic.ClientInterceptor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class DefaultShelterService implements ShelterService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final StringEncryptor shelterEncryptor;

    public UserDto getShelterById(Long shelterId) {
        User shelter = userRepository.findUserById(shelterId);
        if (shelter == null || !shelter.getUserType().equals(UserType.SHELTER)) {
            throw new EntityNotFoundException("Schronisko o podanym id nie istnieje");
        }
        User currentUser = getUser();
        UserDto shelterDto = userMapper.toDto(shelter);
        if (currentUser != null && currentUser.getUserType() == UserType.ADMIN) {
            String iban = shelterEncryptor.decrypt(shelterDto.getIban());
            shelterDto.setIban(iban);
        } else {
            shelterDto.setIban(null);
        }
        return shelterDto;
    }

    public List<UserDto> searchShelters(Map<String, String> searchParams) {
        User currentUser = getUser();
        if (currentUser == null || currentUser.getUserType() != UserType.ADMIN) {
            searchParams.put("isDisabled", "false");
            searchParams.remove("approvalStatus");
        }
        searchParams.put("userType", "SHELTER");
        UserSpecification userSpecification = new UserSpecification(searchParams);
        List<UserDto> userDtoList = userMapper.toDtoList(userRepository.findAll(userSpecification));
        if (currentUser != null && currentUser.getUserType() == UserType.ADMIN) {
            exposeIban(userDtoList);
        } else {
            removeIban(userDtoList);
        }
        return userDtoList;
    }

    private void exposeIban(List<UserDto> userDtoList) {
        userDtoList.stream().filter(userDto -> StringUtils.isNotBlank(userDto.getIban()))
                .forEach((userDto) -> {
                    String iban = shelterEncryptor.decrypt(userDto.getIban());
                    userDto.setIban(iban);
                });
    }

    private void removeIban(List<UserDto> userDtoList) {
        userDtoList.forEach((userDto) -> {
            userDto.setIban(null);
        });
    }

    public UserDto update(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    private User getUser() {
        if (ClientInterceptor.isAnyUserLoggedIn()) {
            return userRepository.findUserByEmail(ClientInterceptor.getCurrentUsername());
        }
        return null;
    }
}
