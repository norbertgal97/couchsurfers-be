package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;

public interface UserService {
    PersonalInformationDTO getPersonalInformation(Long userId);

    ProfileDTO getProfile(Long userId);

}
