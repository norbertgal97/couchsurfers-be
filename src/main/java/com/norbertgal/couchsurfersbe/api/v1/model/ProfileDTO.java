package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("about")
    private String about;

    @JsonProperty("work")
    private String work;

    @JsonProperty("user_photos")
    private Set<UserPhotoDTO> userPhotos;
}
