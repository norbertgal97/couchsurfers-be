package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnReservationMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnReservationPreviewMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.StatusDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.AlreadyBookedException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotBookedException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.TooLateToCancelReservationException;
import com.norbertgal.couchsurfersbe.api.v1.model.request.ReservationRequestDTO;
import com.norbertgal.couchsurfersbe.domain.Couch;
import com.norbertgal.couchsurfersbe.domain.Reservation;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.CouchRepository;
import com.norbertgal.couchsurfersbe.repositories.ReservationRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Profile("dev")
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CouchRepository couchRepository;
    private final OwnReservationPreviewMapper ownReservationPreviewMapper;
    private final OwnReservationMapper ownReservationMapper;

    @Autowired
    public ReservationServiceImpl(OwnReservationMapper ownReservationMapper,
                                  OwnReservationPreviewMapper ownReservationPreviewMapper,
                                  ReservationRepository reservationRepository,
                                  UserRepository userRepository,
                                  CouchRepository couchRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.couchRepository = couchRepository;
        this.ownReservationPreviewMapper = ownReservationPreviewMapper;
        this.ownReservationMapper = ownReservationMapper;
    }

    @Override
    public List<OwnReservationPreviewDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ownReservationPreviewMapper::toReservationPreviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnReservationPreviewDTO> getOwnReservations(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(ownReservationPreviewMapper::toReservationPreviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OwnReservationDTO getOwnReservation(Long userId, Long couchId) throws NotFoundException {
        Optional<Reservation> optionalReservation = reservationRepository.findByUserIdAndCouchId(userId, couchId);

        if (optionalReservation.isEmpty())
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Reservation is not found!").build());

        return ownReservationMapper.toOwnReservationDTO(optionalReservation.get());
    }

    @Override
    public OwnReservationDTO bookCouch(ReservationRequestDTO reservationRequestDTO) throws NotFoundException, NotBookedException, AlreadyBookedException {
        Optional<User> optionalUser = userRepository.findById(reservationRequestDTO.getUserId());
        Optional<Couch> optionalCouch = couchRepository.findById(reservationRequestDTO.getCouchId());

        if (optionalCouch.isEmpty())
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());

        if (optionalUser.isEmpty())
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("User is not found!").build());

        User user = optionalUser.get();
        Couch couch = optionalCouch.get();

        Optional<Reservation> optionalReservation = reservationRepository.findByUserIdAndCouchId(user.getId(), couch.getId());
        if (optionalReservation.isPresent())
            throw new AlreadyBookedException(StatusDTO.builder().timestamp(new Date()).errorCode(400).errorMessage("You have already booked this accommodation!").build());

        Reservation reservation = new Reservation();
        reservation.setCouch(couch);
        reservation.setUser(user);
        reservation.setStartDate(reservationRequestDTO.getStartDate());
        reservation.setEndDate(reservationRequestDTO.getEndDate());
        reservation.setNumberOfGuests(reservationRequestDTO.getNumberOfGuests());

        user.getReservations().add(reservation);

        userRepository.save(user);

        optionalReservation = reservationRepository.findByUserIdAndCouchId(user.getId(), couch.getId());
        if (optionalReservation.isEmpty())
            throw new NotBookedException(StatusDTO.builder().timestamp(new Date()).errorCode(400).errorMessage("The accommodation could not be booked!").build());

        return ownReservationMapper.toOwnReservationDTO(optionalReservation.get());
    }

    @Override
    public void cancelReservation(Long userId, Long couchId) throws NotFoundException, TooLateToCancelReservationException {
        Optional<Reservation> optionalReservation = reservationRepository.findByUserIdAndCouchId(userId, couchId);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            long diff = reservation.getStartDate().getTime() - new Date().getTime();
            if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 15) {
                reservationRepository.delete(reservation);
            } else {
                throw new TooLateToCancelReservationException(StatusDTO.builder().timestamp(new Date()).errorCode(400).errorMessage("It is too late to cancel reservation!").build());
            }
        } else {
            throw new NotFoundException(StatusDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Reservation is not found!").build());
        }
    }

}
