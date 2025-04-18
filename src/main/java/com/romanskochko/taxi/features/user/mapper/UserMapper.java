package com.romanskochko.taxi.features.user.mapper;

import com.romanskochko.taxi.features.user.dto.UserCreateDto;
import com.romanskochko.taxi.features.user.dto.UserDto;
import com.romanskochko.taxi.features.user.entity.User;
import com.romanskochko.taxi.features.user.repository.projection.UserAuthView;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = SPRING)
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUser(UserCreateDto userCreateDto);
    User fromAuthView(UserAuthView userAuthView);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "authorities", ignore = true)
    void update(UserDto source, @MappingTarget User target);
}
