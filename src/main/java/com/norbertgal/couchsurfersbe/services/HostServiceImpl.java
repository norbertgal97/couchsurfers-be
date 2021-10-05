package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.CouchMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnHostedCouchMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.HostDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.StatusDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.domain.Couch;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.CouchRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Profile("dev")
@Service
public class HostServiceImpl implements HostService {
    private final OwnHostedCouchMapper ownHostedCouchMapper;
    private final CouchMapper couchMapper;
    private final UserRepository userRepository;
    private final CouchRepository couchRepository;

    @Autowired
    public HostServiceImpl(CouchMapper couchMapper,
                           OwnHostedCouchMapper ownHostedCouchMapper,
                           UserRepository userRepository,
                           CouchRepository couchRepository) {
        this.couchMapper = couchMapper;
        this.ownHostedCouchMapper = ownHostedCouchMapper;
        this.userRepository = userRepository;
        this.couchRepository = couchRepository;
    }

    @Override
    public OwnHostedCouchDTO getOwnHostedCouch(Long userId) throws NotFoundException, UnknownUserException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new UnknownUserException(StatusDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());

        User user = optionalUser.get();
        Couch couch = user.getCouch();

        if (couch == null)
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());

        return ownHostedCouchMapper.toOwnHostedCouchDTO(couch);
    }

    @Override
    public HostDTO hostCouch(Long userId, Long couchId, HostDTO request) throws NotFoundException, WrongIdentifierException, EmptyFieldsException, UnknownUserException {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Couch> optionalCouch = couchRepository.findById(couchId);

        if (optionalCouch.isEmpty())
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());

        if (optionalUser.isEmpty())
            throw new UnknownUserException(StatusDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());

        User user = optionalUser.get();
        Couch couch = optionalCouch.get();

        if (!user.getId().equals(couch.getId())) {
            throw new WrongIdentifierException(StatusDTO.builder().timestamp(new Date()).errorCode(403).errorMessage("You can't access this resource!").build());
        }

        if (request.getHosted() == null) {
            throw new EmptyFieldsException(StatusDTO.builder()
                    .timestamp(new Date())
                    .errorCode(422)
                    .errorMessage("Hosted is empty!")
                    .build());
        }

        couch.setHosted(request.getHosted());

        Couch hostedCouch = couchRepository.save(couch);

        HostDTO response = new HostDTO();
        response.setHosted(hostedCouch.getHosted());

        return response;
    }

}
