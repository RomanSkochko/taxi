package com.romanskochko.taxi.features.ride.dto;

import java.time.OffsetDateTime;

public record RideListDto(String id,
                          OffsetDateTime scheduledTime,
                          OffsetDateTime endTime) {
}
