package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewListDTO;
import com.norbertgal.couchsurfersbe.services.ReservationService;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public OwnReservationPreviewListDTO getListOfReservations() {
        return new OwnReservationPreviewListDTO(reservationService.getAllReservations());
    }

    @GetMapping(value = "/query/reservations")
    @ResponseStatus(HttpStatus.OK)
    public OwnReservationPreviewListDTO getOwnReservations(@RequestParam(name = "userid") Long userId) {
        return new OwnReservationPreviewListDTO(reservationService.getOwnReservations(userId));
    }

    @GetMapping(value = "/query/reservation")
    @ResponseStatus(HttpStatus.OK)
    public OwnReservationDTO getOwnReservation(@RequestParam(name = "userid") Long userId, @RequestParam(name = "couchid") Long couchId) {
        return reservationService.getOwnReservation(userId, couchId);
    }
}
