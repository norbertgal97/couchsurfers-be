package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.ReviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.api.v1.model.ReviewRequestDTO;

import java.util.List;


public interface ReviewService {
    List<ReviewDTO> getReviewsForSpecificCouch(Long couchId) throws NotFoundException;

    ReviewDTO createReview(ReviewRequestDTO request, Long userId) throws NotFoundException, EmptyFieldsException, UnknownUserException;
}
