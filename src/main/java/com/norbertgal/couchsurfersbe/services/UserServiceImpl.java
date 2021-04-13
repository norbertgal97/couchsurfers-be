package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.PersonalInformationMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.ProfileMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.UserMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.StatusDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.UserDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.AlreadyRegisteredEmailException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.request.LoginRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Profile("dev")
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonalInformationMapper personalInformationMapper;
    private final ProfileMapper profileMapper;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PersonalInformationMapper personalInformationMapper,
                           ProfileMapper profileMapper,
                           UserMapper userMapper,
                           AuthenticationManager authenticationManager,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse) {
        this.userRepository = userRepository;
        this.personalInformationMapper = personalInformationMapper;
        this.profileMapper = profileMapper;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public PersonalInformationDTO getPersonalInformation(Long userId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("PersonalInformation is not found!").build());

        return personalInformationMapper.toPersonalInformationDTO(optionalUser.get());
    }

    @Override
    public ProfileDTO getProfile(Long userId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Profile is not found!").build());

        return profileMapper.toProfileDTO(optionalUser.get());
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty())
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("User is not found!").build());

        return optionalUser.get();
    }

    @Override
    public UserDetails login(LoginRequestDTO request) throws BadCredentialsException {
        UsernamePasswordAuthenticationToken authenticationTokenRequest = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(authenticationTokenRequest);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        return (UserDetails) authentication.getPrincipal();
    }

    @Override
    public Boolean logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(
                    httpServletRequest,
                    httpServletResponse,
                    authentication);
        }
        return true;
    }

    @Override
    public UserDTO register(SignUpRequestDTO request) throws AlreadyRegisteredEmailException {
        User user = new User();
        user.setCreatedAt(new Date());
        user.setEmail(request.getEmail());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());

        if (optionalUser.isEmpty())
            throw new AlreadyRegisteredEmailException(StatusDTO.builder().timestamp(new Date()).errorCode(400).errorMessage("You have already registered with this email address!").build());

        return userMapper.userToUserDTO(optionalUser.get());
    }
}
