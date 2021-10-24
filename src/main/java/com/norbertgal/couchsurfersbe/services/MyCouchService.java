package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.*;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MyCouchService {
    CouchDTO createCouch(CouchDTO couchDTO, long userId) throws NotFoundException, EmptyFieldsException, EntityAlreadyExistsException, UnknownUserException;

    CouchDTO updateCouch(Map<String, Object> fields, long userId, Long couchId) throws NotFoundException, EmptyFieldsException, WrongIdentifierException;

    CouchDTO getCouch(Long couchId, Long userId) throws NotFoundException;

    CouchPreviewDTO getNewestCouch() throws NotFoundException;

    List<CouchPhotoDTO> uploadImages(Long couchId, MultipartFile[] images, Long userId) throws WrongIdentifierException, NotFoundException, EmptyFileException, IOException;

    byte[] downloadImage(Long couchId, Long imageId, Long userId) throws WrongIdentifierException, NotFoundException;

    MessageDTO deleteImages(Long couchId, FileDeleteDTO request, Long userId) throws WrongIdentifierException, NotFoundException;
}
