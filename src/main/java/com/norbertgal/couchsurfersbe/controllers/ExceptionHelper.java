package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.StatusDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ExceptionHelper {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<StatusDTO> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {TooLateToCancelReservationException.class})
    public ResponseEntity<StatusDTO> handleTooLateToCancelReservationException(TooLateToCancelReservationException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotBookedException.class})
    public ResponseEntity<StatusDTO> handleNotBookedException(NotBookedException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AlreadyRegisteredEmailException.class})
    public ResponseEntity<StatusDTO> handleAlreadyRegisteredEmailException(AlreadyRegisteredEmailException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<StatusDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(StatusDTO.builder().timestamp(new Date()).errorCode(400).errorMessage("Invalid username/password supplied").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AlreadyBookedException.class})
    public ResponseEntity<StatusDTO> handleAlreadyBookedException(AlreadyBookedException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.BAD_REQUEST);
    }

}
