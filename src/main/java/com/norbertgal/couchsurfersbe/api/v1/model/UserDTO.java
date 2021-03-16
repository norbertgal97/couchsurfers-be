package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.norbertgal.couchsurfersbe.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String nickname;

    private String about;

    private String work;

    private Gender gender;

    private Date dateOfBirth;

    private Date lastLogin;

    @JsonProperty("created_at")
    private Date createdAt;

    private boolean notification;
}
