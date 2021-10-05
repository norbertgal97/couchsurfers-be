package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchDTO;
import com.norbertgal.couchsurfersbe.domain.Couch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses =  CouchPhotoMapper.class )
public interface OwnHostedCouchMapper {

    @Mappings({
            @Mapping(target = "couchPhotoId", source = "couch.couchPhotos", qualifiedByName = "firstElement"),
            @Mapping(target = "about", source = "couch.about"),
            @Mapping(target = "name", source = "couch.name"),
            @Mapping(target = "couchId", source = "couch.id"),
            @Mapping(target = "hosted", source = "couch.hosted")
    })
    OwnHostedCouchDTO toOwnHostedCouchDTO(Couch couch);
}
