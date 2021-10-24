package com.norbertgal.couchsurfersbe.repositories;

import com.norbertgal.couchsurfersbe.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    @Query(value = "SELECT SUM(r.numberOfGuests) FROM Reservation r WHERE r.couch.id = :couchId AND r.startDate <= :onDate AND r.endDate > :onDate")
    Integer queryNumberOfGuestsOnSpecificDate(@Param("couchId") Long couchId, @Param("onDate") Date onDate);

    Optional<Reservation> findByUserIdAndCouchId(Long userId, Long couchId);

    @Query(value = "SELECT r.id FROM Reservation r WHERE r.user.id = :userId AND r.couch.id = :couchId AND r.startDate <= :onDate AND r.endDate > :onDate")
    List<Long> queryOwnReservationsBetweenDates(@Param("userId") Long userId, @Param("couchId") Long couchId, @Param("onDate") Date onDate);

    @Query(value = "SELECT c.id FROM Couch c WHERE c.id IN (SELECT r.couch.id FROM Reservation r " +
            "WHERE r.couch.id NOT IN :couchIds " +
            "AND r.startDate <= :onDate " +
            "AND r.endDate > :onDate " +
            "AND r.couch.location.city = :city " +
            "GROUP BY r.couch.id, r.couch.numberOfGuests " +
            "HAVING r.couch.numberOfGuests - SUM(r.numberOfGuests) < :guests) " +
            "OR c.id IN (SELECT r2.couch.id FROM Reservation r2 " +
            "WHERE r2.couch.id NOT IN :couchIds " +
            "AND r2.startDate <= :onDate " +
            "AND r2.endDate > :onDate " +
            "AND r2.user.id = :userId " +
            "AND r2.couch.location.city = :city)")
    List<Long> queryReservedCouchesBetweenDates(@Param("couchIds") Set<Long> couchIds,
                                                @Param("userId") Long userId,
                                                @Param("onDate") Date onDate,
                                                @Param("guests") Long guests,
                                                @Param("city") String city);

    @Query(value = "SELECT c.id FROM Couch c WHERE c.id IN (SELECT r.couch.id FROM Reservation r " +
            "WHERE r.startDate <= :onDate " +
            "AND r.endDate > :onDate " +
            "AND r.couch.location.city = :city " +
            "GROUP BY r.couch.id, r.couch.numberOfGuests " +
            "HAVING r.couch.numberOfGuests - SUM(r.numberOfGuests) < :guests) " +
            "OR c.id IN (SELECT r2.couch.id FROM Reservation r2 " +
            "WHERE r2.startDate <= :onDate " +
            "AND r2.endDate > :onDate " +
            "AND r2.user.id = :userId " +
            "AND r2.couch.location.city = :city)")
    List<Long> queryReservedCouchesBetweenDatesWithoutList(@Param("userId") Long userId,
                                                           @Param("onDate") Date onDate,
                                                           @Param("guests") Long guests,
                                                           @Param("city") String city);

}
