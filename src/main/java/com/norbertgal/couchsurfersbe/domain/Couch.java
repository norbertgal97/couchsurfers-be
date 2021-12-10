package com.norbertgal.couchsurfersbe.domain;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "couch")
public class Couch {

    @Id
    @Column(name = "host_id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "number_of_guests")
    private Integer numberOfGuests;

    @NotNull
    @Column(name = "number_of_rooms")
    private Integer numberOfRooms;

    @Column(name = "about", length = 1000)
    private String about;

    @Column(name = "amenities")
    private String amenities;

    @NotNull
    @Column(name = "price")
    private Double price;

    @NotNull
    @Column(name = "hosted")
    private Boolean hosted;

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

    @OneToOne
    @MapsId
    @JoinColumn(name = "host_id")
    private User user;

    @OneToMany(mappedBy = "couch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CouchPhoto> couchPhotos = new ArrayList<>();

}
