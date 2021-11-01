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
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotBookedException.class})
    public ResponseEntity<StatusDTO> handleNotBookedException(NotBookedException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EmptyFieldsException.class})
    public ResponseEntity<StatusDTO> handleEmptyFieldsException(EmptyFieldsException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {EmptyFileException.class})
    public ResponseEntity<StatusDTO> handleEmptyFileException(EmptyFileException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<StatusDTO> handleIOException(IOException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {UnknownUserException.class})
    public ResponseEntity<StatusDTO> handleUnknownUserException(UnknownUserException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {EntityAlreadyExistsException.class})
    public ResponseEntity<StatusDTO> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {WrongIdentifierException.class})
    public ResponseEntity<StatusDTO> handleWrongIdentifierException(WrongIdentifierException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AlreadyRegisteredEmailException.class})
    public ResponseEntity<StatusDTO> handleAlreadyRegisteredEmailException(AlreadyRegisteredEmailException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<StatusDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(StatusDTO.builder().timestamp(new Date()).errorCode(400).errorMessage("Invalid username/password supplied").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AlreadyBookedException.class})
    public ResponseEntity<StatusDTO> handleAlreadyBookedException(AlreadyBookedException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotEnoughFreeSpaceException.class})
    public ResponseEntity<StatusDTO> handleNotEnoughFreeSpaceException(NotEnoughFreeSpaceException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

}
