package shelter.backend.admin.service;

import org.springframework.stereotype.Service;
import shelter.backend.admin.model.AdminShelterResponseDto;
import shelter.backend.admin.model.AdminResponseConverter;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.mapper.UserMapper;
import shelter.backend.rest.model.specification.UserSpecification;
import shelter.backend.storage.repository.AddressRepository;
import shelter.backend.storage.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShelterAdminService implements AdminService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;

    public ShelterAdminService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.userMapper = new UserMapper(userRepository, addressRepository);
    }

    @Override
    public List<AdminShelterResponseDto> getShelters() {//TODO change response body
        List<User> shelterList = userRepository.findAllByShelterNameIsNotNull();
        return shelterList.stream()
                .map(AdminResponseConverter::toAdminGetShelterResponse)
                .collect(Collectors.toList());
    }

    public List<UserDto> search(Map<String, String> searchParams) {
        UserSpecification userSpecification = new UserSpecification(searchParams);
        return userMapper.toDtoList(userRepository.findAll(userSpecification));
    }
}
