package com.norbertgal.couchsurfersbe.services.dummy;

import com.norbertgal.couchsurfersbe.api.v1.model.*;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.TooLateToCancelReservationException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.WrongIdentifierException;
import com.norbertgal.couchsurfersbe.api.v1.model.request.ReservationRequestDTO;
import com.norbertgal.couchsurfersbe.services.ReservationService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Profile("dummy")
@Service
public class ReservationServiceDummy implements ReservationService {

    @Override
    public List<OwnReservationPreviewDTO> getOwnReservations(Long userId) {
        return getOwnReservationDTOs();
    }

    @Override
    public OwnReservationDTO getOwnReservationDetails(Long reservationId, Long userId) throws NotFoundException, WrongIdentifierException {
        return null;
    }

    @Override
    public ReserveDTO bookCouch(ReservationRequestDTO reservationRequestDTO, Long userId) throws NotFoundException {
        return null;
    }

    @Override
    public StatusDTO cancelReservation(Long userId, Long couchId) throws NotFoundException, TooLateToCancelReservationException {
        return new StatusDTO(" ");
    }

    private List<OwnReservationPreviewDTO> getOwnReservationDTOs() {
        OwnReservationPreviewDTO reservation = new OwnReservationPreviewDTO();

        CouchDTO couchDTO = new CouchDTO();
        couchDTO.setId(1L);
        couchDTO.setAbout("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
        couchDTO.setAmenities("Wifi, Dedicated workspace");
        couchDTO.setName("One Bedroom Apartment");
        couchDTO.setNumberOfGuests(3);
        couchDTO.setNumberOfRooms(2);
        couchDTO.setPrice(10.3);

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setBuildingNumber("79/a");
        locationDTO.setCity("London");
        locationDTO.setStreet("Arodene Rd");
        locationDTO.setZipCode("1223");
        couchDTO.setLocation(locationDTO);

        /*List<CouchPhotoDTO> couchPhotoDTOs = new ArrayList<>();
        CouchPhotoDTO couchPhotoDTO = new CouchPhotoDTO();
        couchPhotoDTO.setPhoto("asd12312141r152".getBytes());
        couchPhotoDTOs.add(couchPhotoDTO);
        couchDTO.setCouchPhotos(couchPhotoDTOs);
        couchDTO.getCouchPhotos().add(couchPhotoDTO);*/

        reservation.setEndDate(new GregorianCalendar(2021, Calendar.FEBRUARY, 15).getTime());
        reservation.setStartDate(new GregorianCalendar(2021, Calendar.FEBRUARY, 11).getTime());
        //reservation.setAmenities(couchDTO.getAmenities());
        //reservation.setCouchPhoto(couchPhotoDTO);
        //reservation.setLocation(couchDTO.getLocation());
        //reservation.setRating(4.3);

        List<OwnReservationPreviewDTO> reservations = new ArrayList<>();
        reservations.add(reservation);

        return reservations;
    }
}
