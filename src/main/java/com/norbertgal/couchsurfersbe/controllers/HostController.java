package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchListDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.request.HostRequestDTO;
import com.norbertgal.couchsurfersbe.services.HostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(HostController.BASE_URL)
public class HostController {
    public static final String BASE_URL = "/api/v1/hosts";

    private final HostService hostService;

    public HostController(HostService hostService) {
        this.hostService = hostService;
    }

    @GetMapping(value = "/query/host")
    public ResponseEntity<OwnHostedCouchListDTO> getOwnHostedCouches(@RequestParam(name = "userId") Long userId) {
        return new ResponseEntity<>(new OwnHostedCouchListDTO(hostService.getOwnHostedCouches(userId)), HttpStatus.OK);
    }

    @PostMapping(value = "/host")
    public ResponseEntity<CouchDTO> hostCouch(@RequestBody HostRequestDTO request) throws NotFoundException {
        return new ResponseEntity<>(hostService.hostCouch(request.getUserId(), request.getCouchId()), HttpStatus.OK);
    }

}
