package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouchPreviewDTO {

    @JsonProperty("couch_id")
    private Long couchId;

    @JsonProperty("couch_photo_id")
    private String couchPhotoId;

    private String name;

    private String city;

    private Double price;

}
