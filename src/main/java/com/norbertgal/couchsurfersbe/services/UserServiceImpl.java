package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.PersonalInformationMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.ProfileMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Profile("dev")
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonalInformationMapper personalInformationMapper;
    private final ProfileMapper profileMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PersonalInformationMapper personalInformationMapper,
                           ProfileMapper profileMapper) {
        this.userRepository = userRepository;
        this.personalInformationMapper = personalInformationMapper;
        this.profileMapper = profileMapper;
    }

    @Override
    public PersonalInformationDTO getPersonalInformation(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() ? personalInformationMapper.toPersonalInformationDTO(user.get()) : new PersonalInformationDTO();
    }

    @Override
    public ProfileDTO getProfile(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() ? profileMapper.toProfileDTO(user.get()) : new ProfileDTO();
    }

}
