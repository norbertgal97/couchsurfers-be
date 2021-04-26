package com.norbertgal.couchsurfersbe.repositories;

import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    Optional<Reservation> findByUserIdAndCouchId(Long userId, Long couchId);
}
