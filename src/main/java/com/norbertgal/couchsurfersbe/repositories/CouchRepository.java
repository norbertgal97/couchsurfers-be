package com.norbertgal.couchsurfersbe.repositories;

import com.norbertgal.couchsurfersbe.domain.Couch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouchRepository extends JpaRepository<Couch, Long> {
}
