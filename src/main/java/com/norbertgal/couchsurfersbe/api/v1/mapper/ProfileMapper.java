package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    ProfileDTO toProfileDTO(User user);

}
