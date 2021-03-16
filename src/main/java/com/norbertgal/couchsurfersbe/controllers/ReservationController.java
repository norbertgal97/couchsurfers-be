package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationListDTO;
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
    public OwnReservationListDTO getListOfReservations() {
        return new OwnReservationListDTO(reservationService.getAllReservations());
    }

    @GetMapping(value = "/query")
    @ResponseStatus(HttpStatus.OK)
    public OwnReservationListDTO getOwnReservations(@RequestParam(name = "userid") Long userId) {
        System.out.println("userId = " + userId);
        return new OwnReservationListDTO(reservationService.getOwnReservations(userId));
    }
}
