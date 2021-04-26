package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.api.v1.model.request.ReservationRequestDTO;

import java.util.List;

public interface ReservationService {

    List<OwnReservationPreviewDTO> getAllReservations();

    List<OwnReservationPreviewDTO> getOwnReservations(Long userId);

    OwnReservationDTO getOwnReservation(Long userId, Long couchId) throws NotFoundException;

    OwnReservationDTO bookCouch(ReservationRequestDTO reservationRequestDTO) throws NotFoundException, NotBookedException, AlreadyBookedException, NotEnoughFreeSpaceException;

    void cancelReservation(Long userId, Long couchId) throws NotFoundException, TooLateToCancelReservationException;
}
