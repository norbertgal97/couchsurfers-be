package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.UserDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.AlreadyRegisteredEmailException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.request.LoginRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    PersonalInformationDTO getPersonalInformation(Long userId) throws NotFoundException;

    ProfileDTO getProfile(Long userId) throws NotFoundException;

    User findByEmail(String email) throws NotFoundException;

    UserDetails login(LoginRequestDTO request) throws BadCredentialsException;

    Boolean logout();

    UserDTO register(SignUpRequestDTO request) throws AlreadyRegisteredEmailException;
}
