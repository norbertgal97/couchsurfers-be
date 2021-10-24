package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchDTO;
import com.norbertgal.couchsurfersbe.domain.Couch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = CouchPhotoMapper.class)
public interface CouchMapper {
    CouchMapper INSTANCE = Mappers.getMapper(CouchMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "couch.id"),
            @Mapping(target = "name", source = "couch.name"),
            @Mapping(target = "numberOfGuests", source = "couch.numberOfGuests"),
            @Mapping(target = "numberOfRooms", source = "couch.numberOfRooms"),
            @Mapping(target = "about", source = "couch.about"),
            @Mapping(target = "amenities", source = "couch.amenities"),
            @Mapping(target = "price", source = "couch.price"),
            @Mapping(target = "couchPhotos", source = "couch.couchPhotos", qualifiedByName = "urls"),
            @Mapping(target = "location.zipCode", source = "couch.location.zipCode"),
            @Mapping(target = "location.city", source = "couch.location.city"),
            @Mapping(target = "location.street", source = "couch.location.street"),
            @Mapping(target = "location.buildingNumber", source = "couch.location.buildingNumber")
    })
    CouchDTO couchToCouchDTO(Couch couch);

    Couch couchDTOtoCouch(CouchDTO couchDTO);
}
