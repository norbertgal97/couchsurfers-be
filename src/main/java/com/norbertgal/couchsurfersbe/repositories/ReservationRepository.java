package com.norbertgal.couchsurfersbe.repositories;

import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    Reservation findByUserIdAndCouchId(Long userId, Long couchId);
}
