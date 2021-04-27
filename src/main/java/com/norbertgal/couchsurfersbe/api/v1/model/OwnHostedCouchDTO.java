package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnHostedCouchDTO {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("couch_id")
    private Long couchId;

    @JsonProperty("couch_photo")
    private CouchPhotoDTO couchPhoto;

    private String about;

    private String amenities;

}
