package shelter.backend.admin.service;

import org.springframework.stereotype.Service;
import shelter.backend.admin.model.AdminShelterResponse;
import shelter.backend.admin.model.AdminResponseConverter;
import shelter.backend.rest.model.entity.User;
import shelter.backend.storage.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShelterAdminService implements AdminService{

    private final UserRepository userRepository;

    public ShelterAdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<AdminShelterResponse> getShelters() {
        List<User> shelterList = userRepository.findAllByShelterNameIsNotNull();
        return shelterList.stream()
                .map(AdminResponseConverter::toAdminGetShelterResponse)
                .collect(Collectors.toList());
    }
}
