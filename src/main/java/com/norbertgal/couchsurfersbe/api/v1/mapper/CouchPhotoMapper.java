package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchPhotoDTO;
import com.norbertgal.couchsurfersbe.domain.CouchPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouchPhotoMapper {
    CouchPhotoMapper INSTANCE = Mappers.getMapper(CouchPhotoMapper.class);

    List<CouchPhotoDTO> toCouchPhotoDTOList(List<CouchPhoto> couchPhotos);

    @Mappings({
            @Mapping(target = "photo", source ="couchPhoto.photo")
    })
    CouchPhotoDTO toCouchPhotoDTO(CouchPhoto couchPhoto);

    @Named("firstElement")
    default CouchPhotoDTO listToCouchPhotoDTO(List<CouchPhoto> couchPhotos) {
        return couchPhotos.isEmpty() ? new CouchPhotoDTO() : new CouchPhotoDTO(couchPhotos.get(0).getPhoto());
    }
}
