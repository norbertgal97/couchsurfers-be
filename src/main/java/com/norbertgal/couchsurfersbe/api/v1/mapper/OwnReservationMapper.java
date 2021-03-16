package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OwnReservationMapper {
    OwnReservationMapper INSTANCE = Mappers.getMapper(OwnReservationMapper.class);

    @Mappings({
            @Mapping(target = "couch", source = "reservation.couch")
    })
    OwnReservationDTO reservationToReservationDTO(Reservation reservation);

    Reservation reservationDTOtoReservation(OwnReservationDTO reservationDTO);
}
