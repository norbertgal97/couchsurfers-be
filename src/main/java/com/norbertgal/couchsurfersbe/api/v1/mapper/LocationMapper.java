package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.LocationDTO;
import com.norbertgal.couchsurfersbe.domain.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    LocationDTO toLocationDTO(Location location);

    Location toLocation(LocationDTO locationDTO);
}
