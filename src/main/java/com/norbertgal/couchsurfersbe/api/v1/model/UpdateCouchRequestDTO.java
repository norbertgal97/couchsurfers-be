package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCouchRequestDTO {

    private String name;

    @JsonProperty("number_of_guests")
    private Integer numberOfGuests;

    @JsonProperty("number_of_rooms")
    private Integer numberOfRooms;

    private String about;

    private String amenities;

    private Double price;

    private LocationDTO location;

}
