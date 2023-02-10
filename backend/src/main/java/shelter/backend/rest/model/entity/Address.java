package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "address")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String city;

    private String postal_code;

    private String  building_number;

    private String flat_number;

    private String phone;

    private String KRS_number;

    @OneToOne(mappedBy = "address")
    @Setter
    private User user;

}
