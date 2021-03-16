package com.norbertgal.couchsurfersbe.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reservation")
public class Reservation {

    @EmbeddedId
    private ReservationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("couchId")
    private Couch couch;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

}
