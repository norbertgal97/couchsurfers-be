package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;

import java.util.List;

public interface HostService {
    public List<OwnHostedCouchDTO> getOwnHostedCouches(Long userId);
    public CouchDTO hostCouch(Long userId, Long couchId) throws NotFoundException;
}
