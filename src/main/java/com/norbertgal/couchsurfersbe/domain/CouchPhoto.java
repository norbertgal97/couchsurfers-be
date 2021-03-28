package com.norbertgal.couchsurfersbe.domain;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "couch_photo")
public class CouchPhoto extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "photo", columnDefinition = "BLOB")
    @Lob
    private byte[] photo;
}
