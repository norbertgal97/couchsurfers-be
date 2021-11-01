package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.PersonalInformationDTO;
import com.norbertgal.couchsurfersbe.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UserPhotoMapper.class)
public interface PersonalInformationMapper {
    PersonalInformationMapper INSTANCE = Mappers.getMapper(PersonalInformationMapper.class);

    PersonalInformationDTO toPersonalInformationDTO(User user);
}
