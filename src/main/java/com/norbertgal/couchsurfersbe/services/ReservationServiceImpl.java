package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.OwnReservationMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnReservationDTO;
import com.norbertgal.couchsurfersbe.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Profile("dev")
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final OwnReservationMapper ownReservationMapper;

    @Autowired
    public ReservationServiceImpl(OwnReservationMapper ownReservationMapper, ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        this.ownReservationMapper = ownReservationMapper;
    }

    @Override
    public List<OwnReservationDTO> getAllReservations() {
        return StreamSupport.stream(reservationRepository.findAll().spliterator(), false)
                .map(ownReservationMapper::reservationToReservationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnReservationDTO> getOwnReservations(Long userId) {
        return StreamSupport.stream(reservationRepository.findByUserId(userId).spliterator(), false)
                .map(ownReservationMapper::reservationToReservationDTO)
                .collect(Collectors.toList());
    }
}
