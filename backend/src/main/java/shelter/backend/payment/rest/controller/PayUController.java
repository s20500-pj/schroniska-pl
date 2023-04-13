//package shelter.backend.payment.rest.controller;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import shelter.backend.payment.service.PayUAuthenticationService;
//import shelter.backend.rest.model.dtos.UserDto;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/payment")
//public class PayUController {
//
//    private final PayUAuthenticationService payUAuthenticationService;
//
//    @GetMapping(value = "/payu-callback", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<UserDto> donate(@PathVariable @NotNull Long id) {
//        return ResponseEntity.ok(shelterService.getShelterById(id));
//    }
//
//}
