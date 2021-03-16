package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnReservationDTO {

    @JsonProperty("number_of_guests")
    private int numberOfGuests;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("couch")
    private CouchDTO couch;

}
