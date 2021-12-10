package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewDTO;
import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring", uses = CouchPhotoMapper.class)
public interface OwnReservationPreviewMapper {
    OwnReservationPreviewMapper INSTANCE = Mappers.getMapper(OwnReservationPreviewMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "reservation.id"),
            @Mapping(target = "couchId", source = "reservation.couch.id"),
            @Mapping(target = "couchPhotoId", source = "reservation.couch.couchPhotos", qualifiedByName = "firstElement"),
            @Mapping(target = "name", source = "reservation.couch.name"),
            @Mapping(target = "city", source = "reservation.couch.location.city"),
            @Mapping(target = "price", source = "reservation.couch.price"),
            @Mapping(target = "startDate", source = "reservation.startDate"),
            @Mapping(target = "endDate", source = "reservation.endDate"),
            @Mapping(target = "active", source = "reservation.endDate", qualifiedByName = "activeReservation"),
            @Mapping(target = "numberOfGuests", source = "reservation.numberOfGuests")
    })
    OwnReservationPreviewDTO toReservationPreviewDTO(Reservation reservation);

    @Named("activeReservation")
    default Boolean listToCouchPhotoDTO(Date endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate;

        try {
            parsedDate = formatter.parse(formatter.format(new Date()));
        } catch (ParseException exc) {
            return null;
        }

        return !endDate.before(parsedDate);
    }

}
