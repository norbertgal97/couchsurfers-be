package com.norbertgal.couchsurfersbe.domain;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hosted_by_user")
public class HostedByUser {
    @EmbeddedId
    private HostedByUserId id = new HostedByUserId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("couchId")
    private Couch couch;

}
