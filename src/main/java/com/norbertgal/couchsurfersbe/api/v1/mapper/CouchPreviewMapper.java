package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchPreviewDTO;
import com.norbertgal.couchsurfersbe.domain.Couch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = CouchPhotoMapper.class)
public interface CouchPreviewMapper {
    CouchPreviewMapper INSTANCE = Mappers.getMapper(CouchPreviewMapper.class);

    @Mappings({
            @Mapping(target = "couchId", source = "couch.id"),
            @Mapping(target = "name", source = "couch.name"),
            @Mapping(target = "price", source = "couch.price"),
            @Mapping(target = "couchPhotoId", source = "couch.couchPhotos", qualifiedByName = "firstElement"),
            @Mapping(target = "city", source = "couch.location.city"),
    })
    CouchPreviewDTO couchToCouchPreviewDTO(Couch couch);

    List<CouchPreviewDTO> couchListToCouchPreviewDTOList(List<Couch> couches);
}
