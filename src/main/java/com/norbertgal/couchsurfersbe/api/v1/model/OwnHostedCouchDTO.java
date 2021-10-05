package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnHostedCouchDTO {
    @JsonProperty("couch_id")
    private Long couchId;

    @JsonProperty("couch_photo_id")
    private Long couchPhotoId;

    private String name;

    private String about;

    private Boolean hosted;
}