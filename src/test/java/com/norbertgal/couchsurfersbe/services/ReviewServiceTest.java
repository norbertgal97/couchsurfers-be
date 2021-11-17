package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.BaseTest;
import com.norbertgal.couchsurfersbe.api.v1.model.ReviewDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ReviewRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.domain.Couch;
import com.norbertgal.couchsurfersbe.domain.Review;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.CouchRepository;
import com.norbertgal.couchsurfersbe.repositories.ReviewRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class ReviewServiceTest extends BaseTest {

    @Autowired
    private ReviewService reviewService;

    @MockBean
    private CouchRepository couchRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        Couch couch = setUpCouch();
        User user = setUpUser();
        Review review = setUpReview();

        user.setCouch(couch);
        couch.setUser(user);
        review.setCouch(couch);
        review.setUser(user);

        doReturn(Optional.of(user)).when(userRepository).findById(1L);
        doReturn(Optional.of(couch)).when(couchRepository).findById(1L);
        doReturn(review).when(reviewRepository).save(any());
    }

    @Test
    @DisplayName("Create a review without exceptions")
    void createReview() {
        ReviewRequestDTO requestDTO = new ReviewRequestDTO();
        requestDTO.setCouchId(1L);
        requestDTO.setDescription("This is a description");

        try {
            ReviewDTO review = reviewService.createReview(requestDTO, 1L);
            Assertions.assertEquals("This is a description", review.getDescription());
            Assertions.assertEquals(1, review.getId());
            Assertions.assertEquals("Norbert Gál", review.getName());

        } catch (NotFoundException | EmptyFieldsException | UnknownUserException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Create a review with invalid request")
    void createReviewWithInvalidRequest() {
        ReviewRequestDTO requestDTO = new ReviewRequestDTO();
        requestDTO.setCouchId(1L);

        Exception exception = assertThrows(EmptyFieldsException.class, () -> {
            reviewService.createReview(requestDTO, 1L);
        });

        String expectedMessage = "Empty fields!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Create a review with wrong user id")
    void createReviewWithWrongUserId() {
        ReviewRequestDTO requestDTO = new ReviewRequestDTO();
        requestDTO.setCouchId(1L);
        requestDTO.setDescription("This is a description");

        Exception exception = assertThrows(UnknownUserException.class, () -> {
            reviewService.createReview(requestDTO, 2L);
        });

        String expectedMessage = "User is not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Get reviews for specific couch without exception")
    void getReviewsForSpecificCouch() {
        List<Review> reviews = setUpReviews();
        doReturn(reviews).when(reviewRepository).findAllByCouchId(1L);

        try {
            List<ReviewDTO> reviewDTOs = reviewService.getReviewsForSpecificCouch(1L);
            Assertions.assertEquals(2, reviewDTOs.size());
        } catch (NotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Get reviews for specific couch with wring couch id")
    void getReviewsForSpecificCouchWithWrongCouchId() {
        List<Review> reviews = setUpReviews();
        doReturn(reviews).when(reviewRepository).findAllByCouchId(1L);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            reviewService.getReviewsForSpecificCouch(2L);
        });

        String expectedMessage = "Couch is not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
