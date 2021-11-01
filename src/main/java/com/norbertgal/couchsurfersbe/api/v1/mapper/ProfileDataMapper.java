package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDataDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UserPhotoMapper.class)
public interface ProfileDataMapper {
    ProfileDataMapper INSTANCE = Mappers.getMapper(ProfileDataMapper.class);

    ProfileDataDTO toProfileDataDTO(User user);
}
