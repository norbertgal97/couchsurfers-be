package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;

import java.util.List;

public interface ReservationService {

    List<OwnReservationDTO> getAllReservations();

    List<OwnReservationDTO> getOwnReservations(Long userId);
}
