package shelter.backend.adoption.rest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.adoption.service.VirtualAdoptionService;
import shelter.backend.payment.payu.rest.model.req.OrderDataRequest;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/adoption")
public class VirtualAdoptionController {

    private final VirtualAdoptionService virtualAdoptionService;

    private final OrderDataRequest requestData;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/virtual/{animalId}/{amount}")
    ResponseEntity<String> beginVirtualAdoption(@PathVariable @NotNull Long animalId, @PathVariable @NotNull Long amount, HttpServletRequest request, HttpServletResponse response) {
        requestData.setIpAddress(request.getRemoteAddr());
        String redirect_uri = virtualAdoptionService.beginVirtualAdoption(animalId, amount);
        return ResponseEntity.status(HttpStatus.OK)
                .body(redirect_uri);
    }
}
