package shelter.backend.rest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import shelter.backend.rest.model.dtos.AddressDto;

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
    private String postalCode;
    private String buildingNumber;
    private String flatNumber;
    private String phone;
    private String krsNumber;
    @OneToOne(mappedBy = "address")
    @Setter
    private User user;

    public Address toEntity(AddressDto addressDto) {
        this.id = addressDto.getId();
        this.street = addressDto.getStreet();
        this.city = addressDto.getCity();
        this.postalCode = addressDto.getPostalCode();
        this.buildingNumber = addressDto.getBuildingNumber();
        this.flatNumber = addressDto.getFlatNumber();
        this.phone = addressDto.getPhone();
        this.krsNumber = addressDto.getKrsNumber();
        return this;
    }

    public AddressDto dto() {
        return AddressDto.builder()
                .id(id)
                .street(street)
                .city(city)
                .postalCode(postalCode)
                .buildingNumber(buildingNumber)
                .flatNumber(flatNumber)
                .phone(phone)
                .krsNumber(krsNumber)
                .build();
    }
}