package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnReservationMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnReservationPreviewMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.*;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
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

import java.util.*;
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
    public List<OwnReservationPreviewDTO> getOwnReservations(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(ownReservationPreviewMapper::toReservationPreviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OwnReservationDTO getOwnReservationDetails(Long reservationId, Long userId) throws NotFoundException, WrongIdentifierException {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isEmpty())
            throw new NotFoundException(ErrorDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Reservation is not found!").build());

        Reservation reservation = optionalReservation.get();

        if (!reservation.getUser().getId().equals(userId)) {
            throw new WrongIdentifierException(ErrorDTO.builder().timestamp(new Date()).errorCode(403).errorMessage("You can't access this resource!").build());
        }

        return ownReservationMapper.toOwnReservationDTO(optionalReservation.get());
    }

    @Override
    public ReserveDTO bookCouch(ReservationRequestDTO reservationRequestDTO, Long userId) throws NotFoundException, AlreadyBookedException, NotEnoughFreeSpaceException, UnknownUserException {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Couch> optionalCouch = couchRepository.findById(reservationRequestDTO.getCouchId());

        if (optionalUser.isEmpty())
            throw new UnknownUserException(ErrorDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());

        if (optionalCouch.isEmpty())
            throw new NotFoundException(ErrorDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());

        User user = optionalUser.get();
        Couch couch = optionalCouch.get();

        Date current = reservationRequestDTO.getStartDate();

        Set<Long> ownReservationsBetweenDates = new HashSet<>();

        while (current.before(reservationRequestDTO.getEndDate())) {
            List<Long> ownReservationsOnSpecificDate = reservationRepository.queryOwnReservationsBetweenDates(optionalUser.get().getId(), optionalCouch.get().getId(), current);

            if (ownReservationsOnSpecificDate != null && !ownReservationsOnSpecificDate.isEmpty() ) {
                ownReservationsBetweenDates.addAll(ownReservationsOnSpecificDate);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, 1);
            current = calendar.getTime();
        }

        if (!ownReservationsBetweenDates.isEmpty()) {
            throw new AlreadyBookedException(ErrorDTO.builder().timestamp(new Date()).errorCode(422).errorMessage("You have already booked this accommodation!").build());
        }

        current = reservationRequestDTO.getStartDate();

        while (current.before(reservationRequestDTO.getEndDate())) {
            Integer numberOfGuestsOnSpecificDate = reservationRepository.queryNumberOfGuestsOnSpecificDate(couch.getId(), current);

            if (numberOfGuestsOnSpecificDate != null) {
                System.out.println("Number of reservations on specific day: " + numberOfGuestsOnSpecificDate + "   " + current);

                if (couch.getNumberOfGuests() - numberOfGuestsOnSpecificDate < reservationRequestDTO.getNumberOfGuests())
                    throw new NotEnoughFreeSpaceException(ErrorDTO.builder().timestamp(new Date()).errorCode(422).errorMessage("Could not reserve enough space for guests").build());
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, 1);
            current = calendar.getTime();
        }

        Reservation reservation = new Reservation();
        reservation.setCouch(couch);
        reservation.setUser(user);
        reservation.setStartDate(reservationRequestDTO.getStartDate());
        reservation.setEndDate(reservationRequestDTO.getEndDate());
        reservation.setNumberOfGuests(reservationRequestDTO.getNumberOfGuests());

        user.getReservations().add(reservation);

        userRepository.save(user);

        return new ReserveDTO(true);
    }

    @Override
    public StatusDTO cancelReservation(Long reservationId, Long userId) throws NotFoundException, TooLateToCancelReservationException, WrongIdentifierException {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isEmpty())
            throw new NotFoundException(ErrorDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Reservation is not found!").build());

        if (!optionalReservation.get().getUser().getId().equals(userId))
            throw new WrongIdentifierException(ErrorDTO.builder().timestamp(new Date()).errorCode(403).errorMessage("You can't access this resource!").build());

        long diff = optionalReservation.get().getStartDate().getTime() - new Date().getTime();
        if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 2) {
            reservationRepository.deleteById(reservationId);
        } else {
            throw new TooLateToCancelReservationException(ErrorDTO.builder().timestamp(new Date()).errorCode(409).errorMessage("It is too late to cancel reservation!").build());
        }

        return new StatusDTO("Reservation is successfully cancelled!");
    }

}
