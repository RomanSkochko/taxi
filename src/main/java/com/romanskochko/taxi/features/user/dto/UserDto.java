package com.romanskochko.taxi.features.user.dto;

import com.romanskochko.taxi.features.user.entity.UserPhoto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
public class UserDto {

    String id;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    String name;

    @Email(message = "Invalid email format")
    String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    String phone;

    UserPhoto userPhoto;
}
