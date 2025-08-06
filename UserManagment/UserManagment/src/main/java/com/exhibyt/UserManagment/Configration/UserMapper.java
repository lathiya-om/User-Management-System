package com.exhibyt.UserManagment.Configration;

import com.exhibyt.UserManagment.Dto.UserProfileDto;
import com.exhibyt.UserManagment.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
            @Mapping(target = "active", expression = "java(user.isEnabled())")
    })
    UserProfileDto toUserProfileDto(User user);
}
