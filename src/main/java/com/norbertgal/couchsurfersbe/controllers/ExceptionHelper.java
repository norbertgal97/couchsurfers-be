package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.ErrorDTO;
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
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {TooLateToCancelReservationException.class})
    public ResponseEntity<ErrorDTO> handleTooLateToCancelReservationException(TooLateToCancelReservationException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotBookedException.class})
    public ResponseEntity<ErrorDTO> handleNotBookedException(NotBookedException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EmptyFieldsException.class})
    public ResponseEntity<ErrorDTO> handleEmptyFieldsException(EmptyFieldsException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {EmptyFileException.class})
    public ResponseEntity<ErrorDTO> handleEmptyFileException(EmptyFileException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<ErrorDTO> handleIOException(IOException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {UnknownUserException.class})
    public ResponseEntity<ErrorDTO> handleUnknownUserException(UnknownUserException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {EntityAlreadyExistsException.class})
    public ResponseEntity<ErrorDTO> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {WrongIdentifierException.class})
    public ResponseEntity<ErrorDTO> handleWrongIdentifierException(WrongIdentifierException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AlreadyRegisteredEmailException.class})
    public ResponseEntity<ErrorDTO> handleAlreadyRegisteredEmailException(AlreadyRegisteredEmailException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<ErrorDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(ErrorDTO.builder().timestamp(new Date()).errorCode(401).errorMessage("Invalid username/password supplied").build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {AlreadyBookedException.class})
    public ResponseEntity<ErrorDTO> handleAlreadyBookedException(AlreadyBookedException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotEnoughFreeSpaceException.class})
    public ResponseEntity<ErrorDTO> handleNotEnoughFreeSpaceException(NotEnoughFreeSpaceException ex) {
        return new ResponseEntity<>(ex.getStatus(), HttpStatus.CONFLICT);
    }

}
