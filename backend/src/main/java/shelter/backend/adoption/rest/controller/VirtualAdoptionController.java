package shelter.backend.adoption.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.adoption.service.VirtualAdoptionService;
import shelter.backend.rest.model.dtos.AdoptionDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/adoption")
public class VirtualAdoptionController{

    private final VirtualAdoptionService virtualAdoptionService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/virtual/{animalId}/{amount}")
    ResponseEntity<AdoptionDto> beginVirtualAdoption(@PathVariable Long animalId, @PathVariable Long amount) {
        return ResponseEntity.ok(virtualAdoptionService.beginVirtualAdoption(animalId, amount));
    }
}
