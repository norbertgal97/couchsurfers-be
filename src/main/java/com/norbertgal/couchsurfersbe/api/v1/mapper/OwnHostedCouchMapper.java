package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchDTO;
import com.norbertgal.couchsurfersbe.domain.HostedByUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses =  CouchPhotoMapper.class )
public interface OwnHostedCouchMapper {

    @Mappings({
            @Mapping(target = "couchPhoto", source = "hostedByUser.couch.couchPhotos", qualifiedByName = "firstElement"),
            @Mapping(target = "about", source = "hostedByUser.couch.about"),
            @Mapping(target = "amenities", source = "hostedByUser.couch.amenities"),
            @Mapping(target = "couchId", source = "hostedByUser.couch.id"),
            @Mapping(target = "userId", source = "hostedByUser.user.id")
    })
    OwnHostedCouchDTO toOwnHostedCouchDTO(HostedByUser hostedByUser);
}
