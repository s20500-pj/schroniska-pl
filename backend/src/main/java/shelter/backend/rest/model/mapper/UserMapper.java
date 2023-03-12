package shelter.backend.rest.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.Address;
import shelter.backend.rest.model.entity.User;
import shelter.backend.storage.repository.AddressRepository;
import shelter.backend.storage.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserMapper implements DtoEntityMapper<User, UserDto> {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public User toEntity(UserDto userDto) {
        User user = Optional.ofNullable(userDto.getId())
                .flatMap(userRepository::findById)
                .map(entity -> entity.toEntity(userDto))
                .orElseGet(() -> new User().toEntity(userDto));

        if (userDto.getAddress() != null) {
            Address address = Optional.ofNullable(userDto.getAddress().getId())
                    .flatMap(addressRepository::findById)
                    .map(entity -> entity.toEntity(userDto.getAddress()))
                    .orElseGet(() -> new Address().toEntity(userDto.getAddress()));
            user.setAddress(address);
        }
        return user;
    }

    @Override
    public UserDto toDto(User user) {
        return user.toSimpleDto();
    }

    public List<UserDto> toDtoList(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> userDtos.add(user.toSimpleDto()));
        return userDtos;
    }
}

