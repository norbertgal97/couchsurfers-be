package com.norbertgal.couchsurfersbe.repositories;

import com.norbertgal.couchsurfersbe.domain.CouchPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouchPhotoRepository extends JpaRepository<CouchPhoto, Long> {
}
