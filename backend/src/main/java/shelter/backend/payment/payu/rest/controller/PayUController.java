package shelter.backend.payment.payu.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shelter.backend.payment.payu.rest.model.res.OrderStatus;
import shelter.backend.payment.payu.service.PayUService;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PayUController {

    private final PayUService payUService;

    @Value("${shelter.web.security.friendly-origin-url}")
    private String friendlyOriginUrl;

    @GetMapping(value = "/payu-callback/{extOrderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderStatus> handlePaymentCallback(@PathVariable String extOrderId, @RequestParam(required = false) Optional<String> error) {
        ResponseEntity.ok(payUService.checkPaymentStatus(extOrderId, error));
        URI location = URI.create(friendlyOriginUrl + "/payment"); //todo migrate react builds to springs's static resources
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(location)
                .build();
    }

//    @GetMapping(value = "/payu-notification", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<Void> handlePaymentCallback() {
//        payUService.handleNotification();
//        return ResponseEntity.ok().build();
//    } fixme/todo handle notification

}
