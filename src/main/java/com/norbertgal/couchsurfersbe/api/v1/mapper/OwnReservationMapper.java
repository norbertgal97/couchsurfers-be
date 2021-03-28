package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OwnReservationMapper {
    OwnReservationPreviewMapper INSTANCE = Mappers.getMapper(OwnReservationPreviewMapper.class);

    @Mappings({
            @Mapping(target = "couch", source = "reservation.couch"),
            @Mapping(target = "couchId", source = "reservation.couch.id"),
            @Mapping(target = "userId", source = "reservation.user.id")
    })
    OwnReservationDTO toOwnReservationDTO(Reservation reservation);

}
