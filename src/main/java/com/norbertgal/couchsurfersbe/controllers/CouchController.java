package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.*;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.services.CouchService;
import com.norbertgal.couchsurfersbe.services.authentication.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(CouchController.BASE_URL)
public class CouchController {
    public static final String BASE_URL = "/api/v1/couches";

    private final CouchService couchService;

    @Autowired
    public CouchController(CouchService couchService) {
        this.couchService = couchService;
    }

    @PostMapping(value = "/")
    public ResponseEntity<CouchDTO> createCouch(@RequestBody CouchDTO request, @AuthenticationPrincipal UserDetailsImpl userDetails) throws EmptyFieldsException, EntityAlreadyExistsException, UnknownUserException{
        return new ResponseEntity<>(couchService.createCouch(request, userDetails.getUserId()), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CouchDTO> updateCouch(@RequestBody Map<String, Object> fields, @PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws NotFoundException, EmptyFieldsException, WrongIdentifierException {
        return new ResponseEntity<>(couchService.updateCouch(fields, userDetails.getUserId(), id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CouchDTO> getCouch(@PathVariable("id") Long id) throws NotFoundException {
        return new ResponseEntity<>(couchService.getCouch(id), HttpStatus.OK);
    }

    @GetMapping(value = "/newest")
    public ResponseEntity<CouchPreviewDTO> getNewestCouch() throws NotFoundException {
        return new ResponseEntity<>(couchService.getNewestCouch(), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/images")
    public ResponseEntity<List<CouchPhotoDTO>> uploadImage(@PathVariable("id") Long id, @RequestParam("images") MultipartFile[] images, @AuthenticationPrincipal UserDetailsImpl userDetails) throws WrongIdentifierException, EmptyFileException, NotFoundException, IOException {
        return new ResponseEntity<>(couchService.uploadImages(id, images, userDetails.getUserId()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/images/{image_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable("id") Long couchId, @PathVariable("image_id") Long imageId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws WrongIdentifierException, NotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(couchService.downloadImage(couchId, imageId, userDetails.getUserId()));
    }

    @DeleteMapping(value = "/{id}/images")
    public ResponseEntity<StatusDTO> deleteImages(@PathVariable("id") Long couchId, @RequestBody FileDeleteDTO request, @AuthenticationPrincipal UserDetailsImpl userDetails) throws NotFoundException, WrongIdentifierException {
        return new ResponseEntity<>(couchService.deleteImages(couchId, request, userDetails.getUserId()), HttpStatus.OK);
    }

}
