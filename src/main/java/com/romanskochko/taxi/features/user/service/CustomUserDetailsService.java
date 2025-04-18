package com.romanskochko.taxi.features.user.service;

import com.romanskochko.taxi.features.user.mapper.UserMapper;
import com.romanskochko.taxi.features.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Service
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository repository;
    UserMapper mapper;

    @Cacheable(cacheNames = "userDetails", key = "#phone")
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phone) {
        return getRepository()
                .findUserAuthByPhone(phone)
                .map(mapper::fromAuthView)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phone));
    }

}
