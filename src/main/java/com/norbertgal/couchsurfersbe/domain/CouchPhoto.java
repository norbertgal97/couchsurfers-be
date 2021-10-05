package com.norbertgal.couchsurfersbe.domain;

import com.sun.istack.NotNull;
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

    @NotNull
    @Column(name = "uuid", unique = true)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "couch_id", nullable = false)
    private Couch couch;

    @Column(name = "photo", columnDefinition = "MEDIUMBLOB")
    @Lob
    @NotNull
    private byte[] photo;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "type")
    private String type;
}
