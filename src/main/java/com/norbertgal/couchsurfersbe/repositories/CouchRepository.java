package com.norbertgal.couchsurfersbe.repositories;

import com.norbertgal.couchsurfersbe.domain.Couch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CouchRepository extends JpaRepository<Couch, Long> {

    Optional<Couch> findFirstByOrderByIdDesc();

    @Query("FROM Couch c WHERE c.location.city = :city AND c.numberOfGuests >= :guests AND c.hosted = true AND c.id NOT IN :couchIds")
    List<Couch> findAllByCityAndHostedAndEnoughSpace(@Param("couchIds") Set<Long> couchIds, @Param("city") String city, @Param("guests") Integer guests);

    @Query("FROM Couch c WHERE c.location.city = :city AND c.numberOfGuests >= :guests AND c.hosted = true")
    List<Couch> findAllByCityAndHostedAndEnoughSpaceWithoutList(@Param("city") String city, @Param("guests") Integer guests);

}
