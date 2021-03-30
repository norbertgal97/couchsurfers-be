package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.LoginRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public UserDetails login(@RequestBody LoginRequestDTO request) {
        return userService.login(request);
    }

    @PostMapping(value = "/session/register")
    public Boolean register(@RequestBody SignUpRequestDTO request) {
        return userService.register(request);
    }

    @GetMapping(value = "/session/logout")
    public Boolean logout() {
        return userService.logout();
    }

    @GetMapping(value = "/query/personalinformation")
    @ResponseStatus(HttpStatus.OK)
    public PersonalInformationDTO getPersonalInformation(@RequestParam(name = "userid") Long userId) {
        return userService.getPersonalInformation(userId);
    }

    @GetMapping(value = "/query/profile")
    @ResponseStatus(HttpStatus.OK)
    public ProfileDTO getProfile(@RequestParam(name = "userid") Long userId) {
        return userService.getProfile(userId);
    }

}
