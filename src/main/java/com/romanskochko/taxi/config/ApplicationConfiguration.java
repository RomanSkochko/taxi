package com.romanskochko.taxi.config;

import com.romanskochko.taxi.core.exception.GlobalExceptionHandler;
import com.romanskochko.taxi.features.auth.service.AuthenticationService;
import com.romanskochko.taxi.security.filter.JwtAuthenticationFilter;
import com.romanskochko.taxi.security.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService,
                                                           JwtService jwtService,
                                                           AuthenticationService authService,
                                                           GlobalExceptionHandler globalExceptionHandler) {
        return new JwtAuthenticationFilter(userDetailsService, jwtService, authService, globalExceptionHandler);
    }

    @Bean
    public AuthenticationProvider userAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                             UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider provider) {
        return new ProviderManager(provider);
    }
}
