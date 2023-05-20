package shelter.backend.user.rest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shelter.backend.rest.model.dtos.UserDto;
import shelter.backend.user.service.UserService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('SHELTER')")
    @GetMapping
    ResponseEntity<UserDto> getUserInfo() {
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserDto>> search(@RequestBody Map<String, String> searchParams) {
        return ResponseEntity.ok(userService.search(searchParams));
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> update(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.update(userDto));
    }

    @DeleteMapping(value = "/delete/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        userService.delete(id, request, response);
        return ResponseEntity.noContent().build();
    } //TODO dodanie endpointu ustawiającego isDisable na true dla podanego id usera
}
