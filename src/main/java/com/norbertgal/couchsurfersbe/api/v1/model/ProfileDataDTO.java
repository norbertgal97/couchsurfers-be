package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDataDTO {

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("user_photo")
    private UserPhotoDTO userPhoto;
}
