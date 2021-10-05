package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchPhotoDTO;
import com.norbertgal.couchsurfersbe.domain.CouchPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mapper(componentModel = "spring")
public interface CouchPhotoMapper {
    CouchPhotoMapper INSTANCE = Mappers.getMapper(CouchPhotoMapper.class);

    List<CouchPhotoDTO> toCouchPhotoDTOList(List<CouchPhoto> couchPhotos);

    @Mappings({
            @Mapping(target = "photo", source ="couchPhoto.photo")
    })
    CouchPhotoDTO toCouchPhotoDTO(CouchPhoto couchPhoto);

    @Named("ids")
    default List<Long> toCouchPhotoIds(List<CouchPhoto> couchPhotos) {
        List<Long> longList = new ArrayList<>();

        for(CouchPhoto couchPhoto : couchPhotos) {
            longList.add(couchPhoto.getId());
        }

        return longList;
    }

    @Named("firstElement")
    default Long listToCouchPhotoDTO(List<CouchPhoto> couchPhotos) {
        if (couchPhotos.isEmpty()) {
            return null;
        } else {
            Random random = new Random();
            return couchPhotos.get(random.nextInt(couchPhotos.size())).getId();
        }
    }
}
