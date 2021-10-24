package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchPhotoDTO;
import com.norbertgal.couchsurfersbe.domain.CouchPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mapper(componentModel = "spring")
public interface CouchPhotoMapper {
    CouchPhotoMapper INSTANCE = Mappers.getMapper(CouchPhotoMapper.class);

    List<CouchPhotoDTO> toCouchPhotoDTOList(List<CouchPhoto> couchPhotos);

    @Named("urls")
    default List<CouchPhotoDTO> toCouchPhotoIds(List<CouchPhoto> couchPhotos) {
        String BASE_URL = "/api/v1/couches";
        List<CouchPhotoDTO> couchPhotoDTOs = new ArrayList<>();

        if (couchPhotos == null) {
            return couchPhotoDTOs;
        }

        for(CouchPhoto couchPhoto : couchPhotos) {
            CouchPhotoDTO couchPhotoDTO = new CouchPhotoDTO();
            couchPhotoDTO.setId(couchPhoto.getId());
            couchPhotoDTO.setName(couchPhoto.getFileName());
            couchPhotoDTO.setUrl(BASE_URL + "/" + couchPhoto.getCouch().getId() + "/images/" + couchPhoto.getId());

            couchPhotoDTOs.add(couchPhotoDTO);
        }

        return couchPhotoDTOs;
    }

    @Named("firstElement")
    default String listToCouchPhotoDTO(List<CouchPhoto> couchPhotos) {
        String BASE_URL = "/api/v1/couches";

        if (couchPhotos.isEmpty()) {
            return null;
        } else {
            Random random = new Random();
            CouchPhoto couchPhoto = couchPhotos.get(random.nextInt(couchPhotos.size()));
            Long couchPhotoId = couchPhoto.getId();
            Long couchId = couchPhoto.getCouch().getId();

            return BASE_URL + "/" + couchId + "/images/" + couchPhotoId;
        }
    }

    default CouchPhotoDTO toCouchPhotoDTO(CouchPhoto photo) {
        String BASE_URL = "/api/v1/couches";
        CouchPhotoDTO couchPhotoDTO = new CouchPhotoDTO();

        couchPhotoDTO.setId(photo.getId());
        couchPhotoDTO.setName(photo.getFileName());
        couchPhotoDTO.setUrl(BASE_URL + "/" + photo.getCouch().getId() + "/images/" + photo.getId());

        return couchPhotoDTO;
    }
}
