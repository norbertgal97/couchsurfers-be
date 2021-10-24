package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchPreviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.HostDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.OwnHostedCouchDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.WrongIdentifierException;

import java.util.Date;
import java.util.List;

public interface HostService {
    OwnHostedCouchDTO getOwnHostedCouch(Long userId) throws NotFoundException, UnknownUserException;
    HostDTO hostCouch(Long userId, Long couchId, HostDTO request) throws NotFoundException, WrongIdentifierException, EmptyFieldsException, UnknownUserException;
    List<CouchPreviewDTO> filterHostedCouches(Long userId, String city, Integer guests, Date checkin, Date checkout) throws EmptyFieldsException, UnknownUserException;
}
