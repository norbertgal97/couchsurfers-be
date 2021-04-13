package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.LoginRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserDetails> login(@RequestBody LoginRequestDTO request) {
        UserDetails userDetails = userService.login(request);
        if (userDetails != null)
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/session/register")
    public ResponseEntity<Boolean> register(@RequestBody SignUpRequestDTO request) {
        Boolean response = userService.register(request);
        if (response)
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/session/logout")
    @ResponseStatus(HttpStatus.OK)
    public Boolean logout() {
        return userService.logout();
    }

    @GetMapping(value = "/query/personalinformation")
    public ResponseEntity<PersonalInformationDTO> getPersonalInformation(@RequestParam(name = "userid") Long userId) {
        PersonalInformationDTO personalInformation = userService.getPersonalInformation(userId);
        if (personalInformation != null)
            return new ResponseEntity<>(personalInformation, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/query/profile")
    public ResponseEntity<ProfileDTO> getProfile(@RequestParam(name = "userid") Long userId) {
        ProfileDTO profile = userService.getProfile(userId);
        if (profile != null)
            return new ResponseEntity<>(profile, HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
