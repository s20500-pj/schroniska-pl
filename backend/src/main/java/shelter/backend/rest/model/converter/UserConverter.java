package shelter.backend.rest.model.converter;

import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;

import java.util.HashSet;

public class UserConverter {

    public static User toEntity(UserDto userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .shelterName(userDTO.getShelterName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .address(AddressConverter.toEntity(userDTO.getAddress()))
                .roles(new HashSet<>())
                .build();
    }

    public static UserDto dto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setShelterName(user.getShelterName());
        userDto.setEmail(user.getEmail());
        if (user.getAddress() != null) {
            userDto.setAddress(AddressConverter.dto(user.getAddress()));
        }
        return userDto;
    }
}

