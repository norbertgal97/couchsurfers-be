package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.CouchPreviewMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnHostedCouchMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.CouchPreviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.HostDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.StatusDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import com.norbertgal.couchsurfersbe.domain.Couch;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.CouchRepository;
import com.norbertgal.couchsurfersbe.repositories.ReservationRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Profile("dev")
@Service
public class HostServiceImpl implements HostService {
    private final OwnHostedCouchMapper ownHostedCouchMapper;
    private final CouchPreviewMapper couchPreviewMapper;
    private final UserRepository userRepository;
    private final CouchRepository couchRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public HostServiceImpl(OwnHostedCouchMapper ownHostedCouchMapper,
                           CouchPreviewMapper couchPreviewMapper,
                           UserRepository userRepository,
                           CouchRepository couchRepository,
                           ReservationRepository reservationRepository) {
        this.ownHostedCouchMapper = ownHostedCouchMapper;
        this.couchPreviewMapper = couchPreviewMapper;
        this.userRepository = userRepository;
        this.couchRepository = couchRepository;
        this.reservationRepository = reservationRepository;
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

        couch.setReservations(couch.getReservations().stream().filter(reservation -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

           return !reservation.getEndDate().before(calendar.getTime());
        }).collect(Collectors.toList()));

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

    @Override
    public List<CouchPreviewDTO> filterHostedCouches(Long userId, String city, Integer guests, Date checkin, Date checkout) throws EmptyFieldsException, UnknownUserException {
        if(city == null || guests == null || checkin == null || checkout == null) {
            throw new EmptyFieldsException(StatusDTO.builder()
                    .timestamp(new Date())
                    .errorCode(422)
                    .errorMessage("Fields can not be null!")
                    .build());
        }

        if (city.isEmpty() || guests == 0 || checkin.after(checkout) || checkin.equals(checkout)) {
            return new ArrayList<>();
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new UnknownUserException(StatusDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());

        Set<Long> notAvailableCouches = new HashSet<>();

        if (optionalUser.get().getCouch() != null && optionalUser.get().getCouch().getId() != null) {
            notAvailableCouches.add(optionalUser.get().getCouch().getId());
        }

        Date current = checkin;

        while (current.before(checkout)) {
            if (notAvailableCouches.isEmpty()) {
                notAvailableCouches.addAll(reservationRepository.queryReservedCouchesBetweenDatesWithoutList(optionalUser.get().getId(), current, guests.longValue(), city));
            } else {
                notAvailableCouches.addAll(reservationRepository.queryReservedCouchesBetweenDates(notAvailableCouches, optionalUser.get().getId(), current, guests.longValue(), city));
            }

            System.out.println("Not available couches: " + notAvailableCouches.toString());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, 1);
            current = calendar.getTime();
        }

        if (notAvailableCouches.isEmpty()) {
           return couchPreviewMapper.couchListToCouchPreviewDTOList(couchRepository.findAllByCityAndHostedAndEnoughSpaceWithoutList(city, guests));
        }

        return couchPreviewMapper.couchListToCouchPreviewDTOList(couchRepository.findAllByCityAndHostedAndEnoughSpace(notAvailableCouches, city, guests));
    }

}
