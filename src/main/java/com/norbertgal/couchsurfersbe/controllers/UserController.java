package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.*;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.api.v1.model.request.LoginRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.services.UserService;
import com.norbertgal.couchsurfersbe.services.authentication.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {
    public static final String BASE_URL = "/api/v1/users";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/session/login")
    public ResponseEntity<UserDetails> login(@RequestBody LoginRequestDTO request) throws BadCredentialsException {
        UserDetails userDetails = userService.login(request);
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    @PostMapping(value = "/session/register")
    public ResponseEntity<UserDTO> register(@RequestBody SignUpRequestDTO request) throws AlreadyRegisteredEmailException {
        UserDTO userDTO = userService.register(request);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping(value = "/session/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LogoutDTO> logout() {
        LogoutDTO logoutDTO = userService.logout();
        return new ResponseEntity<>(logoutDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/personal-information")
    public ResponseEntity<PersonalInformationDTO> getPersonalInformation(@AuthenticationPrincipal UserDetailsImpl userDetails) throws NotFoundException {
        PersonalInformationDTO personalInformation = userService.getPersonalInformation(userDetails.getUserId());
        return new ResponseEntity<>(personalInformation, HttpStatus.OK);
    }

    @PatchMapping(value = "/personal-information/{id}")
    public ResponseEntity<PersonalInformationDTO> updatePersonalInformation(@RequestBody PersonalInformationDTO request, @PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws UnknownUserException, EmptyFieldsException, WrongIdentifierException {
        return new ResponseEntity<>(userService.updatePersonalInformation(request, userDetails.getUserId(), id), HttpStatus.OK);
    }

    @GetMapping(value = "/profile-data")
    public ResponseEntity<ProfileDataDTO> getProfileData(@AuthenticationPrincipal UserDetailsImpl userDetails) throws UnknownUserException {
        ProfileDataDTO profileData = userService.getProfileData(userDetails.getUserId());
        return new ResponseEntity<>(profileData, HttpStatus.OK);
    }

    @PostMapping(value = "/images")
    public ResponseEntity<UserPhotoDTO> uploadImages(@RequestParam("image") MultipartFile image, @AuthenticationPrincipal UserDetailsImpl userDetails) throws EmptyFileException, UnknownUserException, IOException {
        return new ResponseEntity<>(userService.uploadImage(image, userDetails.getUserId()), HttpStatus.OK);
    }

    @GetMapping(value = "/images/{image_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable("image_id") Long imageId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws NotFoundException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userService.downloadImage(imageId, userDetails.getUserId()));
    }

    @DeleteMapping(value = "/images/{image_id}")
    public ResponseEntity<StatusDTO> deleteImages(@PathVariable("image_id") Long photoId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws WrongIdentifierException {
        return new ResponseEntity<>(userService.deleteImages(photoId, userDetails.getUserId()), HttpStatus.OK);
    }

}
