package com.norbertgal.couchsurfersbe.domain;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "review")
public class Review {

    @EmbeddedId
    private ReviewId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("couchId")
    private Couch couch;

    @Column(name = "stars")
    private int stars;

    @Column(name = "description")
    private String description;
}
