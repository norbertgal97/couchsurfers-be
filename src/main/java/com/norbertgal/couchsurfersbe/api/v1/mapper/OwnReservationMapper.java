package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = CouchMapper.class)
public interface OwnReservationMapper {
    OwnReservationPreviewMapper INSTANCE = Mappers.getMapper(OwnReservationPreviewMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "reservation.id"),
            @Mapping(target = "couch", source = "reservation.couch"),
            @Mapping(target = "startDate", source = "reservation.startDate"),
            @Mapping(target = "endDate", source = "reservation.endDate"),
            @Mapping(target = "numberOfGuests", source = "reservation.numberOfGuests"),
            @Mapping(target = "ownerName", source = "reservation.user.fullName"),
            @Mapping(target = "ownerEmail", source = "reservation.user.email")
    })
    OwnReservationDTO toOwnReservationDTO(Reservation reservation);

}
