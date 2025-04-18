package com.romanskochko.taxi.features.ride.dto;

import com.neovisionaries.i18n.CurrencyCode;
import com.romanskochko.taxi.core.model.constants.TariffType;
import com.romanskochko.taxi.features.ride.enums.RideStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class RideDto {
    String id;
    String passengerId;
    String driverId;
//    Location pickupLocation;
//    Location dropoffLocation;
    OffsetDateTime createTime;
    OffsetDateTime scheduledTime;
    OffsetDateTime endTime;
    TariffType tariffType;
    Integer price;
    CurrencyCode currency;
    RideStatus status;
}
