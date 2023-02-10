package shelter.backend.admin.service;

import shelter.backend.admin.model.AdminShelterResponse;

import java.util.List;

public interface AdminService {
    List<AdminShelterResponse> getShelters();
}
