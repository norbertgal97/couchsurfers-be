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

    private Long id;

    @JsonProperty("couch_id")
    private Long couchId;

    @JsonProperty("couch_photo_id")
    private String couchPhotoId;

    private String name;

    private String city;

    private Double price;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    private Boolean active;

    @JsonProperty("number_of_guests")
    private Integer numberOfGuests;

}
