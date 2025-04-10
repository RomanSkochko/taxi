package com.romanskochko.taxi.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
@FieldDefaults(level = PROTECTED)
public abstract class BaseEntity implements Serializable {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    String id;

}
