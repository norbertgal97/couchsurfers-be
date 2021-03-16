package com.norbertgal.couchsurfersbe.repositories;

import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    Iterable<Reservation> findByUserId(Long userId);
}
