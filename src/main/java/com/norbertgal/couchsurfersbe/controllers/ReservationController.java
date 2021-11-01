package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.*;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.api.v1.model.request.ReservationRequestDTO;
import com.norbertgal.couchsurfersbe.services.ReservationService;
import com.norbertgal.couchsurfersbe.services.authentication.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ReservationController.BASE_URL)
public class ReservationController {
    public static final String BASE_URL = "/api/v1/reservations";

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<OwnReservationPreviewDTO>> getOwnReservations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(reservationService.getOwnReservations(userDetails.getUserId()), HttpStatus.OK);
    }

    @GetMapping(value = "/{reservationId}")
    public ResponseEntity<OwnReservationDTO> getOwnReservationDetails(@PathVariable(name = "reservationId") Long reservationId,
                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) throws NotFoundException, WrongIdentifierException {
        OwnReservationDTO ownReservation = reservationService.getOwnReservationDetails(reservationId, userDetails.getUserId());
        return new ResponseEntity<>(ownReservation, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<ReserveDTO> bookCouch(@RequestBody ReservationRequestDTO request,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) throws NotFoundException, AlreadyBookedException, NotEnoughFreeSpaceException, UnknownUserException {
        return new ResponseEntity<>(reservationService.bookCouch(request, userDetails.getUserId()), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{reservationId}")
    public ResponseEntity<MessageDTO> cancelReservation(@PathVariable(name = "reservationId") Long reservationId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws NotFoundException, TooLateToCancelReservationException, WrongIdentifierException {
        return new ResponseEntity<>(reservationService.cancelReservation(reservationId, userDetails.getUserId()), HttpStatus.OK);
    }
}
