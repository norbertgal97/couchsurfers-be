package com.norbertgal.couchsurfersbe;

import com.norbertgal.couchsurfersbe.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseTest {
    protected User setUpUser() {
        User user = User.builder()
                .fullName("Norbert Gál")
                .email("norbert.gal@asd.com")
                .password("123456")
                .phoneNumber("06201111111")
                .createdAt(new Date())
                .notification(true)
                .build();

        user.setId(1L);

        return user;
    }

    protected Couch setUpCouch() {
        Location location = Location.builder()
                .city("Budapest")
                .zipCode("1113")
                .street("XY street")
                .buildingNumber("11")
                .build();

        Couch couch = Couch.builder()
                .name("XY Apt")
                .numberOfGuests(2)
                .numberOfRooms(2)
                .about("This is a description")
                .amenities("Wifi")
                .price(2.0)
                .hosted(false)
                .location(location)
                .createdAt(new Date())
                .build();

        couch.setId(1L);

        return couch;
    }

    protected Review setUpReview() {
        Review review = Review.builder()
                .description("This is a description")
                .build();

        review.setId(1L);

        return review;
    }

    protected List<Review> setUpReviews() {
        Review review = new Review();
        review.setDescription("Description 1");
        Review review2 = new Review();
        review2.setDescription("Description 2");

        List<Review> reviews = new ArrayList<>();
        reviews.add(review);
        reviews.add(review2);

        return reviews;
    }

    protected Message setUpMessage() {
        Message message = Message.builder()
                .createdAt(new Date())
                .content("Content")
                .build();

        message.setId(1L);

        return message;
    }
}
