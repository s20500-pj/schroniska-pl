package shelter.backend.rest.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.enums.ApprovalStatus;
import shelter.backend.rest.model.enums.UserType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    @Setter
    private String iban;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL)
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
                .iban(iban)
                .address(Objects.nonNull(address) ? address.dto() : null)
                .build();
    }

    public void setAddress(Address address) {
        address.setUser(this);
        this.address = address;
    }
}
