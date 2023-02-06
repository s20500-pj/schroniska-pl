package shelter.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shelter.backend.dtos.UserDto;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String shelterName;
    @Getter
    @Column(unique = true, nullable = false)
    private String email;
    @Getter
    private String password;
    @Getter
    private boolean isDisabled;

    @Getter
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User toEntity(UserDto userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .shelterName(userDTO.getShelterName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .isDisabled(userDTO.isDisabled())
                .build();
    }

    public UserDto dto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setShelterName(shelterName);
        userDto.setEmail(email);
        userDto.setDisabled(isDisabled);
        return userDto;
    }
}
