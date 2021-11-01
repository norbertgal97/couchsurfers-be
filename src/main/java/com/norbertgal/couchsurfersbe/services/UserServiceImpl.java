package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.PersonalInformationMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.ProfileDataMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.UserMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.UserPhotoMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.*;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.api.v1.model.request.LoginRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.request.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.domain.Couch;
import com.norbertgal.couchsurfersbe.domain.CouchPhoto;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.domain.UserPhoto;
import com.norbertgal.couchsurfersbe.repositories.UserPhotoRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Profile("dev")
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPhotoRepository userPhotoRepository;
    private final PersonalInformationMapper personalInformationMapper;
    private final ProfileDataMapper profileDataMapper;
    private final UserMapper userMapper;
    private final UserPhotoMapper userPhotoMapper;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserPhotoRepository userPhotoRepository,
                           PersonalInformationMapper personalInformationMapper,
                           ProfileDataMapper profileDataMapper,
                           UserMapper userMapper,
                           UserPhotoMapper userPhotoMapper,
                           AuthenticationManager authenticationManager,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse) {
        this.userRepository = userRepository;
        this.userPhotoRepository = userPhotoRepository;
        this.personalInformationMapper = personalInformationMapper;
        this.profileDataMapper = profileDataMapper;
        this.userMapper = userMapper;
        this.userPhotoMapper = userPhotoMapper;
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
    public PersonalInformationDTO updatePersonalInformation(PersonalInformationDTO personalInformationDTO, long userId, Long personalInformationId) throws UnknownUserException, EmptyFieldsException, WrongIdentifierException {
        if (personalInformationDTO.getFullName() == null || personalInformationDTO.getFullName().isEmpty()) {
            throw new EmptyFieldsException(StatusDTO.builder().timestamp(new Date()).errorCode(422).errorMessage("Empty fields!").build());
        }

        if (userId != personalInformationId) {
            throw new WrongIdentifierException(StatusDTO.builder().timestamp(new Date()).errorCode(403).errorMessage("You can't access this resource!").build());
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UnknownUserException(StatusDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());
        }

        User user = optionalUser.get();

        user.setFullName(personalInformationDTO.getFullName());
        user.setPhoneNumber(personalInformationDTO.getPhoneNumber());

        User savedUser = userRepository.save(user);

        return personalInformationMapper.toPersonalInformationDTO(savedUser);
    }

    @Override
    public ProfileDataDTO getProfileData(Long userId) throws UnknownUserException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UnknownUserException(StatusDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());
        }

        return profileDataMapper.toProfileDataDTO(optionalUser.get());
    }

    @Override
    public UserPhotoDTO uploadImage(MultipartFile image, Long userId) throws UnknownUserException, EmptyFileException, IOException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new UnknownUserException(StatusDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());
        }

        UserPhoto userPhoto = new UserPhoto();

        if (image.isEmpty()) {
            throw new EmptyFileException(StatusDTO.builder().timestamp(new Date()).errorCode(422).errorMessage("No file has been chosen or the chosen file has no content.").build());
        }

        try {
            userPhoto.setPhoto(image.getBytes());
            userPhoto.setUser(optionalUser.get());
            userPhoto.setFileName(image.getOriginalFilename());
            userPhoto.setType(image.getContentType());
        } catch (java.io.IOException ex) {
            throw new IOException(
                    StatusDTO.builder()
                            .timestamp(new Date())
                            .errorCode(422)
                            .errorMessage("Cannot read content of multipart file").build());
        }

        UserPhoto savedPhoto = userPhotoRepository.save(userPhoto);

        return userPhotoMapper.toUserPhotoDTO(savedPhoto);
    }

    @Override
    @Transactional
    public MessageDTO deleteImages(Long photoId, Long userId) throws WrongIdentifierException {
        if (!photoId.equals(userId)) {
            throw new WrongIdentifierException(StatusDTO.builder().timestamp(new Date()).errorCode(403).errorMessage("You can't access this resource!").build());
        }

        System.out.println("User photo id: " + photoId);

        userPhotoRepository.deleteWherePhotoIdIs(photoId);

        return new MessageDTO("Image is successfully deleted!");
    }

    @Override
    public byte[] downloadImage(Long imageId, Long userId) throws NotFoundException {
        Optional<UserPhoto> optionalUserPhoto = userPhotoRepository.findById(imageId);

        if (optionalUserPhoto.isEmpty()) {
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Image is not found!").build());
        }

        UserPhoto userPhoto = optionalUserPhoto.get();

        return userPhoto.getPhoto();
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
    public LogoutDTO logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(
                    httpServletRequest,
                    httpServletResponse,
                    authentication);
        }
        return new LogoutDTO("Logged out!");
    }

    @Override
    public UserDTO register(SignUpRequestDTO request) throws AlreadyRegisteredEmailException {
        User user = new User();
        user.setCreatedAt(new Date());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());

        if (optionalUser.isPresent())
            throw new AlreadyRegisteredEmailException(StatusDTO.builder().timestamp(new Date()).errorCode(409).errorMessage("You have already registered with this email address!").build());

        User registeredUser = userRepository.save(user);
        return userMapper.userToUserDTO(registeredUser);
    }
}
