package com.romanskochko.taxi.features.auth.controller;

import com.romanskochko.taxi.features.auth.dto.AuthRequest;
import com.romanskochko.taxi.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
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
public class AuthController {

    AuthenticationManager authManager;
    UserDetailsService userService;
    JwtService jwtService;

    @PostMapping("/sign-in")
    public String login(@RequestBody AuthRequest authRequest, HttpServletRequest request) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getPhone(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userService.loadUserByUsername(authRequest.getPhone()));
        } else {
            throw new BadCredentialsException("Invalid phone or password");
        }
    }
}





