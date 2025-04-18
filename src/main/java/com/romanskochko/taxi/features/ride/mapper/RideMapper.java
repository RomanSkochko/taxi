package com.romanskochko.taxi.features.ride.mapper;

import com.romanskochko.taxi.features.ride.dto.RideCreateDto;
import com.romanskochko.taxi.features.ride.dto.RideDto;
import com.romanskochko.taxi.features.ride.entity.Ride;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = SPRING)
public interface RideMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    RideDto toDto(Ride ride);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void update(RideDto source, @MappingTarget Ride target);

    Ride toRide(RideCreateDto dto);
}
