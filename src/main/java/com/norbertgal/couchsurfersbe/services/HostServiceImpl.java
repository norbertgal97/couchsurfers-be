package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.CouchMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnHostedCouchMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.CouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.StatusDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.domain.Couch;
import com.norbertgal.couchsurfersbe.domain.HostedByUser;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.CouchRepository;
import com.norbertgal.couchsurfersbe.repositories.HostedByUserRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Profile("dev")
@Service
public class HostServiceImpl implements HostService {
    private final HostedByUserRepository hostedByUserRepository;
    private final OwnHostedCouchMapper ownHostedCouchMapper;
    private final CouchMapper couchMapper;
    private final UserRepository userRepository;
    private final CouchRepository couchRepository;

    @Autowired
    public HostServiceImpl(CouchMapper couchMapper,
                           OwnHostedCouchMapper ownHostedCouchMapper,
                           HostedByUserRepository hostedByUserRepository,
                           UserRepository userRepository,
                           CouchRepository couchRepository) {
        this.couchMapper = couchMapper;
        this.ownHostedCouchMapper = ownHostedCouchMapper;
        this.hostedByUserRepository = hostedByUserRepository;
        this.userRepository = userRepository;
        this.couchRepository = couchRepository;
    }

    @Override
    public List<OwnHostedCouchDTO> getOwnHostedCouches(Long userId) {
        return hostedByUserRepository.findByUserId(userId).stream()
                .map(ownHostedCouchMapper::toOwnHostedCouchDTO).collect(Collectors.toList());
    }

    @Override
    public CouchDTO hostCouch(Long userId, Long couchId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Couch> optionalCouch = couchRepository.findById(couchId);

        if (optionalCouch.isEmpty())
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());

        if (optionalUser.isEmpty())
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("User is not found!").build());

        User user = optionalUser.get();
        Couch couch = optionalCouch.get();

        HostedByUser hostedByUser = new HostedByUser();
        hostedByUser.setCouch(couch);
        hostedByUser.setUser(user);

        hostedByUserRepository.save(hostedByUser);

        return couchMapper.couchToCouchDTO(couch);
    }

}
