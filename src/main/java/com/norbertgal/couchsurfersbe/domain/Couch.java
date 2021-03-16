package com.norbertgal.couchsurfersbe.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "couch")
public class Couch extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    @Column(name = "number_of_rooms")
    private int numberOfRooms;

    @Column(name = "about")
    private String about;

    @Column(name = "amenities")
    private String amenities;

    @Column(name = "price")
    private Double price;

    @Embedded
    private Location location;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @OneToMany(
            mappedBy = "couch",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(
            mappedBy = "couch",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(
            mappedBy = "couch",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private HostedByUser hostedByUser;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CouchPhoto> couchPhotos = new ArrayList<>();

}
