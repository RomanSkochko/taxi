package com.romanskochko.taxi.features.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanskochko.taxi.AbstractIntegrationTest;
import com.romanskochko.taxi.core.model.enums.Role;
import com.romanskochko.taxi.features.user.dto.UserChangePasswordRequest;
import com.romanskochko.taxi.features.user.dto.UserCreateDto;
import com.romanskochko.taxi.features.user.dto.UserDto;
import com.romanskochko.taxi.features.user.entity.User;
import com.romanskochko.taxi.features.user.repository.UserRepository;
import com.romanskochko.taxi.security.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.romanskochko.taxi.features.user.controller.UsersApi.BY_ID;
import static com.romanskochko.taxi.features.user.controller.UsersApi.CHANGE_PASSWORD;
import static com.romanskochko.taxi.features.user.controller.UsersApi.CREATE;
import static com.romanskochko.taxi.features.user.controller.UsersApi.DELETE;
import static com.romanskochko.taxi.features.user.controller.UsersApi.UPDATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository repository;

    @Autowired
    JwtService jwtService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CacheManager cacheManager;

    String user1Uuid;
    String testUserToken;
    @Value("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIrMzgwNTAxNTAyMDIwIiwiaWF0IjoxNzMzNTAwNDI2LCJleHAiOjE3MzM1ODY4MjZ9.dfQcCLC2VcWkb5_wGOht3FKTEQ1mgqvhNYIK158huKw3ail63O-d5iXXc2-UNzvo5eiL8JhR5hACWj6zmz7m2A")
    String invalidUserToken;
    User testUser1;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        insertUsers();
        testUserToken = jwtService.generateToken(testUser1);
    }

    @BeforeEach
    void clearCache() {
        cacheManager.getCacheNames().forEach(name -> {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
    }

    @Test
    void getUserById_WithValidToken() throws Exception {
        // When/Then
        mockMvc.perform(get(BY_ID, user1Uuid)
                        .header("Authorization", "Bearer " + testUserToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Snow"))
                .andExpect(jsonPath("$.email").value("johnsnow@example.com"))
                .andExpect(jsonPath("$.phone").value("+1234500000"));
    }

    @Test
    void getUserById_WithInvalidToken_ThenReturn401() throws Exception {
        // When/Then
        mockMvc.perform(get(BY_ID, user1Uuid)
                        .header("Authorization", "Bearer " + invalidUserToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void getUserById_WhenUserNotFound_ThenReturn404() throws Exception {
        // When/Then
        mockMvc.perform(get(BY_ID, UUID.randomUUID())
                        .header("Authorization", "Bearer " + testUserToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_WhenUserValid_NoAuthRequired() throws Exception {
        // Given
        UserCreateDto newUser = UserCreateDto.builder()
                .name("New User")
                .password("newUserPass")
                .email("newuser@example.com")
                .phone("+9876543210")
                .build();

        // When/Then
        mockMvc.perform(post(CREATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.phone").value("+9876543210"));

        // Verify
        assertThat(repository.count()).isEqualTo(3);
    }

    @Test
    void createUser_WithInvalidEmailAndPhone_ThenReturn400() throws Exception {
        // Given
        UserCreateDto invalidUser = UserCreateDto.builder()
                .name("New User")
                .password("newUserPass")
                .email("invalid-email")  // Invalid email
                .phone("+120") // Invalid phone
                .build();

        // When/Then
        mockMvc.perform(post(CREATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WhenUserMatchesToken() throws Exception {
        // Given
        UserDto userForUpdate = UserDto.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .build();

        // When/Then
        mockMvc.perform(put(UPDATE, user1Uuid)
                        .header("Authorization", "Bearer " + testUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userForUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        // Verify
        User updatedUser = repository.findById(user1Uuid).orElseThrow();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void updateUser_WhenUserDoesNotMatchToken_ThenReturn403() throws Exception {
        User anotherUser = repository.save(User.builder()
                .name("Another User")
                .password("password5")
                .email("another@example.com")
                .phone("+1111111111")
                .roles(new HashSet<>(Set.of(Role.USER)))
                .build());
        String anotherUserToken = jwtService.generateToken(anotherUser);

        UserDto userForUpdate = UserDto.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .phone("+9999999999")
                .build();

        mockMvc.perform(put(UPDATE, user1Uuid)
                        .header("Authorization", "Bearer " + anotherUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userForUpdate)))
                .andExpect(status().isForbidden());
    }

    @Test
    void changePassword_WithValidRequest() throws Exception {
        // When/Then
        UserChangePasswordRequest request = UserChangePasswordRequest.builder()
                .currentPassword("userTestPass")
                .newPassword("newPassword55@")
                .confirmPassword("newPassword55@")
                .build();

        mockMvc.perform(patch(CHANGE_PASSWORD, user1Uuid)
                        .header("Authorization", "Bearer " + testUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify
        User updatedUser = repository.findById(user1Uuid).orElseThrow();
        assertThat(updatedUser.getName()).isEqualTo("John Snow");
        assertThat(passwordEncoder.matches(request.getNewPassword(), updatedUser.getPassword())).isTrue();

    }

    @Test
    void changePassword_WithInvalidCurrentPassword() throws Exception {
        // Given
        UserChangePasswordRequest request = UserChangePasswordRequest.builder()
                .currentPassword("wrongPassword")
                .newPassword("newPassword55@")
                .confirmPassword("newPassword55@")
                .build();

        // When/Then
        mockMvc.perform(patch(CHANGE_PASSWORD)
                        .header("Authorization", "Bearer " + testUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        // Verify that password wasn't changed
        User user = repository.findById(user1Uuid).orElseThrow();
        assertThat(passwordEncoder.matches("userTestPass", user.getPassword())).isTrue();
    }

    @Test
    void changePassword_WithoutAuthorization() throws Exception {
        // Given
        UserChangePasswordRequest request = UserChangePasswordRequest.builder()
                .currentPassword("userTestPass")
                .newPassword("newPassword55@")
                .confirmPassword("newPassword55@")
                .build();

        // When/Then
        mockMvc.perform(patch(CHANGE_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteUser_WhenUserMatchesToken() throws Exception {
        // Given
        long initialCount = repository.count();

        // When/Then
        mockMvc.perform(delete(DELETE, user1Uuid)
                        .header("Authorization", "Bearer " + testUserToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify
        assertThat(repository.count()).isEqualTo(initialCount - 1);
    }

    @Test
    void deleteUser_WhenUserDoesNotMatchToken_ThenReturn403() throws Exception {
        // Given
        User anotherUser = repository.save(User.builder()
                .name("Another User")
                .password("password5")
                .email("another@example.com")
                .phone("+1111111111")
                .roles(new HashSet<>(Set.of(Role.USER)))
                .build());
        String anotherUserToken = jwtService.generateToken(anotherUser);

        // When/Then
        mockMvc.perform(delete(DELETE, user1Uuid)
                        .header("Authorization", "Bearer " + anotherUserToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void insertUsers() {
        testUser1 = repository.save(User.builder()
                .name("John Snow")
                .password(passwordEncoder.encode("userTestPass"))
                .email("johnsnow@example.com")
                .phone("+1234500000")
                .roles(new HashSet<>(Set.of(Role.USER)))
                .build());
        user1Uuid = String.valueOf(testUser1.getId());

        User testUser2 = User.builder()
                .name("John Snow2")
                .password(passwordEncoder.encode("userTestPass"))
                .email("johnsnow@example.com")
                .phone("+380501502020")
                .roles(new HashSet<>(Set.of(Role.USER)))
                .build();

        repository.save(testUser2);
    }
}
