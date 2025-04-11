package com.romanskochko.taxi.security.config;

import com.romanskochko.taxi.security.filter.CustomRateLimitFilter;
import com.romanskochko.taxi.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {
    public static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/users/registration"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            CustomRateLimitFilter customRateLimitFilter,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(WHITE_LIST_URL).permitAll();
                    registry.requestMatchers("/api/v1/users/**").hasRole("USER");
                    registry.requestMatchers("/api/v1/drivers/**", "/api/v1/car-brands", "/api/v1/car-types").hasRole("DRIVER");
                    registry.anyRequest().authenticated();
                })
                .addFilterBefore(customRateLimitFilter, DisableEncodeUrlFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .build();
    }
}
