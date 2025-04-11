package com.romanskochko.taxi.features.user.controller;

import com.romanskochko.taxi.features.user.dto.UserChangePasswordRequest;
import com.romanskochko.taxi.features.user.dto.UserCreateDto;
import com.romanskochko.taxi.features.user.dto.UserDto;
import com.romanskochko.taxi.features.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.romanskochko.taxi.features.user.controller.UsersApi.BY_ID;
import static com.romanskochko.taxi.features.user.controller.UsersApi.CHANGE_PASSWORD;
import static com.romanskochko.taxi.features.user.controller.UsersApi.CREATE;
import static com.romanskochko.taxi.features.user.controller.UsersApi.DELETE;
import static com.romanskochko.taxi.features.user.controller.UsersApi.UPDATE;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {

    UserService service;

    @GetMapping(BY_ID)
    public UserDto getUserById(@PathVariable String id) {
        return service.findUserById(id);
    }

    @ResponseStatus(CREATED)
    @PostMapping(CREATE)
    public UserDto createUser(@Valid @RequestBody UserCreateDto dto) {
        return service.create(dto);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping(UPDATE)
    public UserDto updateUser(@PathVariable String id, @Valid @RequestBody UserDto dto) {
        return service.update(id, dto);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(DELETE)
    public void deleteUser(@PathVariable String id) {
        service.delete(id);
    }

    @PreAuthorize("authentication != null && authentication.principal != null")
    @PatchMapping(CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordRequest request,
                                            Principal principal) {
        service.changePassword(request.getCurrentPassword(), request.getNewPassword(),
                                request.getConfirmPassword(), principal);
        return ResponseEntity.ok().build();
    }

}
