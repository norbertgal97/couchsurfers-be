package com.norbertgal.couchsurfersbe.api.v1.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("couch_id")
    private Long couchId;

    @JsonProperty("start_date")
    private Date startDate;

    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("number_of_guests")
    private Integer numberOfGuests;

}
