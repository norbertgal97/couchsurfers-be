package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.ReviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.api.v1.model.ReviewRequestDTO;
import com.norbertgal.couchsurfersbe.services.ReviewService;
import com.norbertgal.couchsurfersbe.services.authentication.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ReviewController.BASE_URL)
public class ReviewController {
    public static final String BASE_URL = "/api/v1/reviews";

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviewsForSpecificCouch(@RequestParam(name = "couchid") Long couchId) throws NotFoundException {
        return new ResponseEntity<>(reviewService.getReviewsForSpecificCouch(couchId), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewRequestDTO request, @AuthenticationPrincipal UserDetailsImpl userDetails) throws NotFoundException, UnknownUserException, EmptyFieldsException {
        return new ResponseEntity<>(reviewService.createReview(request, userDetails.getUserId()), HttpStatus.CREATED);
    }
}