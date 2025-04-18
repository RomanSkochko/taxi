package com.romanskochko.taxi.features.user.service;

import com.romanskochko.taxi.config.CacheManager;
import com.romanskochko.taxi.core.exception.CustomApplicationException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Set;

import static com.romanskochko.taxi.core.exception.ErrorCode.PASSWORD_MISMATCH;
import static com.romanskochko.taxi.core.exception.ErrorCode.SAME_PASSWORD;
import static com.romanskochko.taxi.core.exception.ErrorCode.WRONG_PASSWORD;
import static lombok.AccessLevel.PRIVATE;

@Service
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
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
        return mapper.toUserDto(user);
    }

    @Transactional
    public UserDto create(UserCreateDto dto) {
        User user = mapper.toUser(dto);
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.USER));
        User savedUser = repository.save(user);

        eventPublisher.publishEvent(new UserCreatedEvent(user.getId()));
        return mapper.toUserDto(savedUser);
    }

    @Transactional
    public UserDto update(String userId, UserDto dto) {
        User found = findById(userId);
        cacheManager.evictUserByPhone(found.getPhone());
        cacheManager.evictUserById(found.getId());
        dto.setId(userId);
        mapper.update(dto, found);
        return mapper.toUserDto(found);
    }

    @Transactional
    public void changePassword(String currentPassword, String newPassword,
                               String confirmPassword, Principal principal) {
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        User user = (User) userDetails;

        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw CustomApplicationException.of("Wrong password", WRONG_PASSWORD);
        }
        if (currentPassword.equals(newPassword)) {
            throw CustomApplicationException.of("Same password", SAME_PASSWORD);
        }
        if (!newPassword.equals(confirmPassword)) {
            throw CustomApplicationException.of("Passwords do not match", PASSWORD_MISMATCH);
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
