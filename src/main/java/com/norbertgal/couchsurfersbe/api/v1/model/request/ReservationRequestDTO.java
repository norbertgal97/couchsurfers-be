package com.norbertgal.couchsurfersbe.api.v1.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

    @JsonProperty("couch_id")
    private Long couchId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("start_date")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("end_date")
    private Date endDate;

    @JsonProperty("number_of_guests")
    private Integer numberOfGuests;

}
