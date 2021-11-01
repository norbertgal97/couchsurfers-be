package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.ReviewDTO;
import com.norbertgal.couchsurfersbe.domain.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    List<ReviewDTO> toReviewDTOList(List<Review> reviewList);

    @Mappings({
            @Mapping(target = "id", source = "review.id"),
            @Mapping(target = "name", source = "review.user.fullName"),
            @Mapping(target = "description", source = "review.description"),
    })
    ReviewDTO toReviewDTO(Review review);

}
