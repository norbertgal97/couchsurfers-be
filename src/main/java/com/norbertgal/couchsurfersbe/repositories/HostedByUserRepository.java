package com.norbertgal.couchsurfersbe.repositories;

import com.norbertgal.couchsurfersbe.domain.HostedByUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostedByUserRepository extends JpaRepository<HostedByUser, Long> {

    List<HostedByUser> findByUserId(Long userId);

}
