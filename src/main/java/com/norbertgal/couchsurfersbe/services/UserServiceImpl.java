package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.PersonalInformationMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.ProfileMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ProfileDTO;
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
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PersonalInformationMapper personalInformationMapper,
                           ProfileMapper profileMapper,
                           AuthenticationManager authenticationManager,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse) {
        this.userRepository = userRepository;
        this.personalInformationMapper = personalInformationMapper;
        this.profileMapper = profileMapper;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public PersonalInformationDTO getPersonalInformation(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(personalInformationMapper::toPersonalInformationDTO).orElse(null);
    }

    @Override
    public ProfileDTO getProfile(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(profileMapper::toProfileDTO).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails login(LoginRequestDTO request) {
        UsernamePasswordAuthenticationToken authenticationTokenRequest = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationTokenRequest);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            return (UserDetails) authentication.getPrincipal();

        } catch (BadCredentialsException ex) {
            return null;
        }
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
    public Boolean register(SignUpRequestDTO request) {
        User user = new User();
        user.setCreatedAt(new Date());
        user.setEmail(request.getEmail());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
