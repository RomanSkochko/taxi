package com.romanskochko.taxi.features.auth.controller;

import com.romanskochko.taxi.core.exception.ErrorResponse;
import com.romanskochko.taxi.features.auth.dto.AuthRequest;
import com.romanskochko.taxi.security.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "Authentication management API")
public class AuthController {

    AuthenticationManager authManager;
    UserDetailsService userService;
    JwtService jwtService;

    @PostMapping("/sign-in")
    @Operation(
            summary = "User authentication",
            description = "Authenticates a user with phone number and password and returns a JWT token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication successful",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(implementation = String.class, description = "JWT token")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Authentication failed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    public String login(@Valid @RequestBody AuthRequest authRequest, HttpServletRequest request) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getPhone(), authRequest.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid phone or password");
        }

        return jwtService.generateToken(userService.loadUserByUsername(authRequest.getPhone()));
    }
}





