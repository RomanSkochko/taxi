package com.romanskochko.taxi.security.filter;

import com.romanskochko.taxi.features.auth.service.AuthenticationService;
import com.romanskochko.taxi.security.config.SecurityConfiguration;
import com.romanskochko.taxi.security.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final List<String> WHITE_LIST_URL = Arrays.stream(SecurityConfiguration.WHITE_LIST_URL)
            .map(url -> url.replace("**", ""))
            .toList();
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    UserDetailsService userService;
    JwtService jwtService;
    AuthenticationService authenticationService;
    AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            doAuthentication(authHeader);
            Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                    .ifPresent(auth -> ((UsernamePasswordAuthenticationToken) auth)
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request)));
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            LOG.error("JwtAuthenticationFilter Exception:", e);
            authenticationEntryPoint.commence(request, response, new AuthenticationException(e.getMessage()) {});
        }
    }

    private void doAuthentication(String authHeader) {
        String jwt = authHeader.substring(7);
        if (!jwtService.isTokenValid(jwt)) return;

        String userPhone = jwtService.extractUserPhone(jwt);
        UserDetails userDetails = userService.loadUserByUsername(userPhone);
        if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticationService.authenticateUser(userDetails);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return WHITE_LIST_URL.stream().anyMatch(path::startsWith);
    }

}


