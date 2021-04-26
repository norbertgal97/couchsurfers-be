package com.norbertgal.couchsurfersbe.repositories;

import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    @Query(value = "SELECT SUM(r.numberOfGuests) FROM Reservation r WHERE r.couch.id = :couchId AND r.startDate <= :onDate AND r.endDate >= :onDate")
    int queryNumberOfGuestsOnSpecificDate(@Param("couchId") Long couchId, @Param("onDate") Date onDate);

    Optional<Reservation> findByUserIdAndCouchId(Long userId, Long couchId);
}
