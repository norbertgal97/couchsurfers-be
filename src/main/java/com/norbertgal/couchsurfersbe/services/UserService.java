package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.LoginRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    PersonalInformationDTO getPersonalInformation(Long userId);

    ProfileDTO getProfile(Long userId);

    User findByEmail(String email);

    UserDetails login(LoginRequestDTO request);

    Boolean logout();

    Boolean register(SignUpRequestDTO request);
}
