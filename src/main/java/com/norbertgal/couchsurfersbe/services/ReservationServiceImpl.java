package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnReservationMapper;
import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnReservationPreviewMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationPreviewDTO;
import com.norbertgal.couchsurfersbe.domain.Reservation;
import com.norbertgal.couchsurfersbe.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Profile("dev")
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final OwnReservationPreviewMapper ownReservationPreviewMapper;
    private final OwnReservationMapper ownReservationMapper;

    @Autowired
    public ReservationServiceImpl(OwnReservationMapper ownReservationMapper,
                                  OwnReservationPreviewMapper ownReservationPreviewMapper,
                                  ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
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
    public OwnReservationDTO getOwnReservation(Long userId, Long couchId) {
        Optional<Reservation> optionalReservation = reservationRepository.findByUserIdAndCouchId(userId, couchId);
        return optionalReservation.map(ownReservationMapper::toOwnReservationDTO).orElse(null);
    }
}
