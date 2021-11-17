package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.*;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.api.v1.model.LoginRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    PersonalInformationDTO getPersonalInformation(Long userId) throws NotFoundException;

    PersonalInformationDTO updatePersonalInformation(PersonalInformationDTO personalInformationDTO, long userId, Long personalInformationId) throws UnknownUserException, EmptyFieldsException, WrongIdentifierException;

    ProfileDataDTO getProfileData(Long userId) throws UnknownUserException;

    UserPhotoDTO uploadImage(MultipartFile image, Long userId) throws UnknownUserException, EmptyFileException, IOException;

    StatusDTO deleteImages(Long photoId, Long userId) throws  WrongIdentifierException;

    byte[] downloadImage(Long imageId, Long userId) throws NotFoundException;

    User findByEmail(String email) throws NotFoundException;

    UserDetails login(LoginRequestDTO request) throws BadCredentialsException;

    LogoutDTO logout();

    UserDTO register(SignUpRequestDTO request) throws AlreadyRegisteredEmailException;
}
