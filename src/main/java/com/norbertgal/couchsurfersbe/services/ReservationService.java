package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.AlreadyBookedException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotBookedException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.TooLateToCancelReservationException;
import com.norbertgal.couchsurfersbe.api.v1.model.request.ReservationRequestDTO;

import java.util.List;

public interface ReservationService {

    List<OwnReservationPreviewDTO> getAllReservations();

    List<OwnReservationPreviewDTO> getOwnReservations(Long userId);

    OwnReservationDTO getOwnReservation(Long userId, Long couchId) throws NotFoundException;

    OwnReservationDTO bookCouch(ReservationRequestDTO reservationRequestDTO) throws NotFoundException, NotBookedException, AlreadyBookedException;

    void cancelReservation(Long userId, Long couchId) throws NotFoundException, TooLateToCancelReservationException;
}
