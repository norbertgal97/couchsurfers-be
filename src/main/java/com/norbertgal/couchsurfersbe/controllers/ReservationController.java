package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewListDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.ReservationRequestDTO;
import com.norbertgal.couchsurfersbe.services.ReservationService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ReservationController.BASE_URL)
public class ReservationController {
    public static final String BASE_URL = "/api/v1/reservations";

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<OwnReservationPreviewListDTO> getListOfReservations() {
        return new ResponseEntity<>(new OwnReservationPreviewListDTO(reservationService.getAllReservations()), HttpStatus.OK);
    }

    @GetMapping(value = "/query/reservations")
    public ResponseEntity<OwnReservationPreviewListDTO> getOwnReservations(@RequestParam(name = "userid") Long userId) {
        return new ResponseEntity<>(new OwnReservationPreviewListDTO(reservationService.getOwnReservations(userId)), HttpStatus.OK);
    }

    @GetMapping(value = "/query/reservation")
    public ResponseEntity<OwnReservationDTO> getOwnReservation(@RequestParam(name = "userid") Long userId, @RequestParam(name = "couchid") Long couchId) {
        OwnReservationDTO ownReservation = reservationService.getOwnReservation(userId, couchId);
        if (ownReservation != null)
            return new ResponseEntity<>(ownReservation, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/book")
    public ResponseEntity<OwnReservationDTO> bookCouch(@RequestBody ReservationRequestDTO request) {
        OwnReservationDTO ownReservation = reservationService.bookCouch(request);
        if (ownReservation != null)
            return new ResponseEntity<>(ownReservation, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/cancel")
    public ResponseEntity<HttpStatus> cancelReservation(@RequestParam(name = "userid") Long userId, @RequestParam(name = "couchid") Long couchId) throws NotFoundException {
        reservationService.cancelReservation(userId, couchId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
