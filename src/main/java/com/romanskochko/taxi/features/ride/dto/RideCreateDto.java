package com.romanskochko.taxi.features.ride.dto;

import com.romanskochko.taxi.core.model.constants.TariffType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record RideCreateDto(@NotNull TariffType tariffType,
                            @NotNull @FutureOrPresent OffsetDateTime scheduledTime,
                            @NotNull @Min(-90) @Max(90) double pickupLatitude,
                            @NotNull @Min(-180) @Max(180) double pickupLongitude,
                            @NotNull @Min(-90) @Max(90) double dropoffLatitude,
                            @NotNull @Min(-180) @Max(180) double dropoffLongitude,
                            String comment) {}

