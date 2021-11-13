package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.StatusDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ReserveDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.api.v1.model.request.ReservationRequestDTO;

import java.util.List;

public interface ReservationService {

    List<OwnReservationPreviewDTO> getOwnReservations(Long userId);

    OwnReservationDTO getOwnReservationDetails(Long reservationId, Long userId) throws NotFoundException, WrongIdentifierException;

    ReserveDTO bookCouch(ReservationRequestDTO reservationRequestDTO, Long userId) throws NotFoundException, AlreadyBookedException, NotEnoughFreeSpaceException, UnknownUserException;

    StatusDTO cancelReservation(Long reservationId, Long userId) throws NotFoundException, TooLateToCancelReservationException, WrongIdentifierException;
}
