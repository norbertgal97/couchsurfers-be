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
@Table(name = "user_photo")
public class UserPhoto extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "photo", columnDefinition = "MEDIUMBLOB")
    @Lob
    @NotNull
    private byte[] photo;

    @NotNull
    @Column(name = "file_name", unique = true)
    private String fileName;

    @Column(name = "type")
    private String type;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
