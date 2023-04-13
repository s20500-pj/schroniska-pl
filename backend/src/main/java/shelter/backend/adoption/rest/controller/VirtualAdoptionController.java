package shelter.backend.adoption.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.adoption.service.VirtualAdoptionService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/adoption")
public class VirtualAdoptionController{

    private final VirtualAdoptionService virtualAdoptionService;
}
