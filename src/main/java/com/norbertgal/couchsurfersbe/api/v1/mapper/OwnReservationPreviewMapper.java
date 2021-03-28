package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewDTO;
import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses =  CouchPhotoMapper.class )
public interface OwnReservationPreviewMapper {
    OwnReservationPreviewMapper INSTANCE = Mappers.getMapper(OwnReservationPreviewMapper.class);

    @Mappings({
            @Mapping(target = "couchPhoto", source = "reservation.couch.couchPhotos", qualifiedByName = "firstElement"),
            @Mapping(target = "location", source = "reservation.couch.location"),
            @Mapping(target = "amenities", source = "reservation.couch.amenities"),
            @Mapping(target = "couchId", source = "reservation.couch.id"),
            @Mapping(target = "userId", source = "reservation.user.id")
    })
    OwnReservationPreviewDTO toReservationPreviewDTO(Reservation reservation);

}
