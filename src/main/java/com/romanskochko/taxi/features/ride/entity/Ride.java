package com.romanskochko.taxi.features.ride.entity;

import com.romanskochko.taxi.core.entity.BaseEntity;
import com.romanskochko.taxi.features.passenger.entity.PassengerProfile;
import com.romanskochko.taxi.core.model.constants.TariffType;
import com.romanskochko.taxi.features.ride.enums.RideStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "rides")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Ride extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_profile_id", nullable = false)
    PassengerProfile passengerProfile;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "driver_profile_id", nullable = false)
//    DriverProfile driverProfile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TariffType tariffType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RideStatus status;

    @Column(nullable = false)
    OffsetDateTime createTime;

    @Column(nullable = false)
    OffsetDateTime scheduledTime;

    OffsetDateTime endTime;

    String comment;

//    Location pickupLocation;
//    Location dropoffLocation;
}