//package shelter.backend.payment.payu.rest.controller;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import shelter.backend.payment.payu.service.PayUService;
//import shelter.backend.rest.model.dtos.UserDto;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/payment")
//public class PayUController {
//
//    private final PayUService payUService;
//
//    @GetMapping(value = "/payu-callback", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<UserDto> donate(@PathVariable @NotNull Long id) {
//        return ResponseEntity.ok(shelterService.getShelterById(id));
//    }
//
//    @GetMapping(value = "/payu-callback", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<UserDto> donate(@PathVariable @NotNull Long id) {
//        return ResponseEntity.ok(shelterService.getShelterById(id));
//    }
//
//}
