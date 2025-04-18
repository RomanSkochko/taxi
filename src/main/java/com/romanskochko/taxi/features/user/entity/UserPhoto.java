package com.romanskochko.taxi.features.user.entity;

import com.romanskochko.taxi.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "user_photos")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserPhoto extends BaseEntity {

    @Column(name = "user_photo", columnDefinition = "TEXT")
    String base64Image;

    @OneToOne(mappedBy = "userPhoto")
    User user;
}
