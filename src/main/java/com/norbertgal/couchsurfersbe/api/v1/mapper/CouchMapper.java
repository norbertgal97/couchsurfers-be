package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.CouchDTO;
import com.norbertgal.couchsurfersbe.domain.Couch;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CouchPhotoMapper.class)
public interface CouchMapper {
    CouchMapper INSTANCE = Mappers.getMapper(CouchMapper.class);

    CouchDTO couchToCouchDTO(Couch couch);

    Couch couchDTOtoCouch(CouchDTO couchDTO);
}
