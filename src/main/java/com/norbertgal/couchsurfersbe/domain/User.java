package com.norbertgal.couchsurfersbe.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "about")
    private String about;

    @Column(name = "work")
    private String work;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "notification")
    private boolean notification;

    @Embedded
    private Location location;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<HostedByUser> hostedByUsers = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Message message;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserPhoto> userPhotos;
}
