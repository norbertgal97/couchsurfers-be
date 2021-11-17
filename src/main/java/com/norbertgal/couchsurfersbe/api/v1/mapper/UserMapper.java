package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.UserDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "fullName", source = "user.fullName"),
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "createdAt", source = "user.createdAt")
    })
    UserDTO userToUserDTO(User user);
}
