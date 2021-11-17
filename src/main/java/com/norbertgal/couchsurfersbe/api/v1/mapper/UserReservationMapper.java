package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.UserReservationDTO;
import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses =  UserPhotoMapper.class )
public interface UserReservationMapper {
    UserReservationMapper INSTANCE = Mappers.getMapper(UserReservationMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "reservation.id"),
            @Mapping(target = "name", source = "reservation.user.fullName"),
            @Mapping(target = "email", source = "reservation.user.email"),
            @Mapping(target = "endDate", source = "reservation.endDate"),
            @Mapping(target = "startDate", source = "reservation.startDate"),
            @Mapping(target = "numberOfGuests", source = "reservation.numberOfGuests"),
            @Mapping(target = "userPhoto", source = "reservation.user.userPhoto")
    })
    UserReservationDTO toUserReservationDTO(Reservation reservation);

}