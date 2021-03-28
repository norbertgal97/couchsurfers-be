package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnReservationPreviewDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("couch_id")
    private Long couchId;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("couch_photo")
    private CouchPhotoDTO couchPhoto;

    private LocationDTO location;

    private String amenities;

    private Double rating;

}
