package com.romanskochko.taxi.features.ride.dto;

import com.romanskochko.taxi.core.model.constants.TariffType;
import jakarta.validation.constraints.NotNull;

public record RideEstimateParams(
        @NotNull Double pickUpLatitude,
        @NotNull Double pickUpLongitude,
        @NotNull Double dropOffLatitude,
        @NotNull Double dropOffLongitude,
        @NotNull TariffType tariffType
) {}
