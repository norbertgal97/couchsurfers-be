package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.ReservationRequestDTO;
import javassist.NotFoundException;

import java.util.List;

public interface ReservationService {

    List<OwnReservationPreviewDTO> getAllReservations();

    List<OwnReservationPreviewDTO> getOwnReservations(Long userId);

    OwnReservationDTO getOwnReservation(Long userId, Long couchId);

    OwnReservationDTO bookCouch(ReservationRequestDTO reservationRequestDTO);

    void cancelReservation(Long userId, Long couchId) throws NotFoundException;
}
