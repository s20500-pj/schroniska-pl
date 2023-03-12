package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.enums.ApprovalStatus;
import shelter.backend.rest.model.enums.UserType;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String shelterName;
    @Column(unique = true, nullable = false)
    private String email;
    @Setter
    private String password;
    @Setter
    private boolean isDisabled;
    @Setter
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String information;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "shelter")
    private List<Animal> animals = new ArrayList<>();

    public User toEntity(UserDto dto) {
        this.id = dto.getId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.shelterName = dto.getShelterName();
        this.email = dto.getEmail();
        this.approvalStatus = dto.getApprovalStatus();
        this.userType = dto.getUserType();
        this.information = dto.getInformation();
        return this;
    }

    public UserDto toSimpleDto() {
        return UserDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .shelterName(shelterName)
                .email(email)
                .isDisabled(isDisabled)
                .approvalStatus(approvalStatus)
                .userType(userType)
                .information(information)
                .address(Objects.nonNull(address) ? address.dto() : null)
                .build();
    }

    public void setAddress(Address address) {
        address.setUser(this);
        this.address = address;
    }
}
