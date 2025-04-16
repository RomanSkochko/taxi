package com.romanskochko.taxi.security.config;

import com.romanskochko.taxi.security.filter.CustomRateLimitFilter;
import com.romanskochko.taxi.security.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import java.time.OffsetDateTime;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {
    public static final String[] APP_WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/users/registration",
    };
    public static final String[] OPEN_API_LIST_URL = {
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            CustomRateLimitFilter customRateLimitFilter,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return httpSecurity
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint()) // Указываем entry point
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(APP_WHITE_LIST_URL).permitAll();
                    registry.requestMatchers(OPEN_API_LIST_URL).permitAll();
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

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            String errorMessage = "Unauthorized access";
            String errorDetails = authException.getMessage();

            String timestamp = OffsetDateTime.now().toString();

            String jsonError = String.format(
                    "{\"status\": 401, \"error\": \"%s\", \"message\": \"%s\", \"timestamp\": \"%s\"}",
                    errorMessage,
                    errorDetails,
                    timestamp
            );

            response.getWriter().write(jsonError);
        };
    }
}
