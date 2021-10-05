package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.LogoutDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.UserDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.AlreadyRegisteredEmailException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.request.LoginRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/query/personalinformation")
    public ResponseEntity<PersonalInformationDTO> getPersonalInformation(@RequestParam(name = "userid") Long userId) throws NotFoundException {
        PersonalInformationDTO personalInformation = userService.getPersonalInformation(userId);
        return new ResponseEntity<>(personalInformation, HttpStatus.OK);
    }

    @GetMapping(value = "/query/profile")
    public ResponseEntity<ProfileDTO> getProfile(@RequestParam(name = "userid") Long userId) throws NotFoundException {
        ProfileDTO profile = userService.getProfile(userId);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

}
