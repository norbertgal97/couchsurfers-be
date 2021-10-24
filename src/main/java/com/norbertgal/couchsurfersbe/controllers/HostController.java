package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchPreviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.HostDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.WrongIdentifierException;
import com.norbertgal.couchsurfersbe.services.HostService;
import com.norbertgal.couchsurfersbe.services.authentication.UserDetailsImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(HostController.BASE_URL)
public class HostController {
    public static final String BASE_URL = "/api/v1/hosts";

    private final HostService hostService;

    public HostController(HostService hostService) {
        this.hostService = hostService;
    }

    @GetMapping(value = "/own")
    public ResponseEntity<OwnHostedCouchDTO> getOwnHostedCouch(@AuthenticationPrincipal UserDetailsImpl userDetails) throws UnknownUserException, NotFoundException {
        return new ResponseEntity<>(hostService.getOwnHostedCouch(userDetails.getUserId()), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<HostDTO> hostCouch(@PathVariable("id") Long id, @RequestBody HostDTO request, @AuthenticationPrincipal UserDetailsImpl userDetails) throws NotFoundException, EmptyFieldsException, WrongIdentifierException, UnknownUserException {
        return new ResponseEntity<>(hostService.hostCouch(userDetails.getUserId(), id, request), HttpStatus.OK);
    }

    @GetMapping(value = "/query")
    public ResponseEntity<List<CouchPreviewDTO>> filterHostedCouches(@RequestParam String city,
                                                                     @RequestParam Integer guests,
                                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkin,
                                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout,
                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) throws EmptyFieldsException, UnknownUserException {
        return new ResponseEntity<>(hostService.filterHostedCouches(userDetails.getUserId(), city, guests, checkin, checkout), HttpStatus.OK);
    }

}
