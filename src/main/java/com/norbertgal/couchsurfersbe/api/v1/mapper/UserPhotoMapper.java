package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.UserPhotoDTO;
import com.norbertgal.couchsurfersbe.domain.UserPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserPhotoMapper {
    UserPhotoMapper INSTANCE = Mappers.getMapper(UserPhotoMapper.class);

    default UserPhotoDTO toUserPhotoDTO(UserPhoto photo) {
        if (photo == null) {
            return null;
        }

        String BASE_URL = "/api/v1/users";
        UserPhotoDTO userPhotoDTO = new UserPhotoDTO();

        userPhotoDTO.setId(photo.getId());
        userPhotoDTO.setName(photo.getFileName());
        userPhotoDTO.setUrl(BASE_URL + "/images/" + photo.getId());

        return userPhotoDTO;
    }
}