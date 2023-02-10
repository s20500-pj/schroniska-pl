package shelter.backend.rest.model.converter;

import shelter.backend.rest.model.dtos.AddressDto;
import shelter.backend.rest.model.entity.Address;

public class AddressConverter {

    public static Address toEntity(AddressDto addressDto) {
        if (addressDto != null) {
            return Address.builder()
                    .street(addressDto.getStreet())
                    .city(addressDto.getCity())
                    .postal_code(addressDto.getPostal_code())
                    .building_number(addressDto.getBuilding_number())
                    .flat_number(addressDto.getFlat_number())
                    .phone(addressDto.getPhone())
                    .KRS_number(addressDto.getKRS_number())
                    .build();
        } else {
            return null;
        }
    }

    public static AddressDto dto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet(address.getStreet());
        addressDto.setCity(address.getCity());
        addressDto.setPostal_code(address.getPostal_code());
        addressDto.setBuilding_number(address.getBuilding_number());
        addressDto.setFlat_number(address.getFlat_number());
        addressDto.setPhone(address.getPhone());
        addressDto.setKRS_number(address.getKRS_number());
        return addressDto;
    }

}
