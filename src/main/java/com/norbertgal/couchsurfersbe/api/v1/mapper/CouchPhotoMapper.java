package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchPhotoDTO;
import com.norbertgal.couchsurfersbe.domain.CouchPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouchPhotoMapper {
    CouchPhotoMapper INSTANCE = Mappers.getMapper(CouchPhotoMapper.class);

    List<CouchPhotoDTO> toDTO(List<CouchPhoto> couchPhotos);
}
