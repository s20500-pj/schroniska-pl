package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import shelter.backend.rest.model.enums.ApprovalStatus;

import java.util.HashSet;
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
    private String password;
    @Setter // check if this is good approach, enabling user by the setter
    private boolean isDisabled;
    @Setter
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @Setter
    private Address address;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}
