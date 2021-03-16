package com.norbertgal.couchsurfersbe.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode
public class ReviewId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "couch_id")
    private Long couchId;

}
