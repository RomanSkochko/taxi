package com.romanskochko.taxi.features.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    String currentPassword;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String newPassword;

    @NotBlank(message = "Password confirmation is required")
    String confirmPassword;
}
