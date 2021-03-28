package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewDTO;

import java.util.List;

public interface ReservationService {

    List<OwnReservationPreviewDTO> getAllReservations();

    List<OwnReservationPreviewDTO> getOwnReservations(Long userId);

    OwnReservationDTO getOwnReservation(Long userId, Long couchId);
}
