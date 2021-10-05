package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.HostDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.WrongIdentifierException;

public interface HostService {
    OwnHostedCouchDTO getOwnHostedCouch(Long userId) throws NotFoundException, UnknownUserException;
    HostDTO hostCouch(Long userId, Long couchId, HostDTO request) throws NotFoundException, WrongIdentifierException, EmptyFieldsException, UnknownUserException;
}
