package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.ReviewMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.ReviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ErrorDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.api.v1.model.request.ReviewRequestDTO;
import com.norbertgal.couchsurfersbe.domain.Couch;
import com.norbertgal.couchsurfersbe.domain.Review;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.CouchRepository;
import com.norbertgal.couchsurfersbe.repositories.ReviewRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Profile("dev")
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CouchRepository couchRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, CouchRepository couchRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.couchRepository = couchRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public List<ReviewDTO> getReviewsForSpecificCouch(Long couchId) throws NotFoundException {
        Optional<Couch> optionalCouch = couchRepository.findById(couchId);

        if (optionalCouch.isEmpty()) {
            throw new NotFoundException(ErrorDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());
        }

        List<Review> reviews = reviewRepository.findAllByCouchId(couchId);

        return reviewMapper.toReviewDTOList(reviews);
    }

    @Override
    public ReviewDTO createReview(ReviewRequestDTO request, Long userId) throws NotFoundException, EmptyFieldsException, UnknownUserException {
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            throw new EmptyFieldsException(ErrorDTO.builder()
                    .timestamp(new Date())
                    .errorCode(422)
                    .errorMessage("Empty fields!")
                    .build());
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new UnknownUserException(ErrorDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());

        Optional<Couch> optionalCouch = couchRepository.findById(request.getCouchId());

        if (optionalCouch.isEmpty())
            throw new NotFoundException(ErrorDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Couch is not found!").build());

        Review review = new Review();
        review.setCouch(optionalCouch.get());
        review.setUser(optionalUser.get());
        review.setDescription(request.getDescription());

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toReviewDTO(savedReview);
    }


}
