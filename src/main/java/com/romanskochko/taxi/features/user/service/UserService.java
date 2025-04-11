package com.romanskochko.taxi.features.user.service;

import com.romanskochko.taxi.config.CacheManager;
import com.romanskochko.taxi.core.model.enums.Role;
import com.romanskochko.taxi.core.service.BaseService;
import com.romanskochko.taxi.features.user.dto.UserCreateDto;
import com.romanskochko.taxi.features.user.dto.UserDto;
import com.romanskochko.taxi.features.user.entity.User;
import com.romanskochko.taxi.features.user.event.UserCreatedEvent;
import com.romanskochko.taxi.features.user.mapper.UserMapper;
import com.romanskochko.taxi.features.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Service
public class UserService extends BaseService<User, String> {
    UserRepository repository;
    UserMapper mapper;
    PasswordEncoder encoder;
    CacheManager cacheManager;
    ApplicationEventPublisher eventPublisher;

    @Cacheable(cacheNames = "userById", key = "#id")
    @Transactional(readOnly = true)
    public UserDto findUserById(String id) {
        User user = findById(id);
        return mapper.toUserDtoFromUser(user);
    }

    @Transactional
    public UserDto create(UserCreateDto dto) {
        User user = mapper.toUserFromCreateDto(dto);
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.USER));
        User savedUser = repository.save(user);

        eventPublisher.publishEvent(new UserCreatedEvent(user.getId()));
        return mapper.toUserDtoFromUser(savedUser);
    }

    @Transactional
    public UserDto update(String userId, UserDto dto) {
        User found = findById(userId);
        cacheManager.evictUserByPhone(found.getPhone());
        cacheManager.evictUserById(found.getId());
        dto.setId(userId);
        mapper.update(dto, found);
        return mapper.toUserDtoFromUser(found);
    }

    @Transactional
    public void changePassword(String currentPassword, String newPassword,
                               String confirmPassword, Principal principal) {
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        User user = (User) userDetails;

        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new AccessDeniedException("Wrong password"); //TODO make custom exceptions
        }
        if (currentPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Same password as before");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        user.setPassword(encoder.encode(newPassword));
        repository.save(user);

        cacheManager.evictUserByPhone(user.getPhone());
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "userDetails", allEntries = true),
            @CacheEvict(cacheNames = "userById", key = "#id")
    })
    @Transactional
    @Override
    public void delete(String id) {
        getRepository().deleteById(id);  //TODO make event on delete
    }

    @Override
    protected Class<User> entityClass() {
        return User.class;
    }
}
