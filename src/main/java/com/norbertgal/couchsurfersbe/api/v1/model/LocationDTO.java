package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

    @JsonProperty("zip_code")
    private int zipCode;

    private String city;

    private String street;

    @JsonProperty("building_number")
    private String buildingNumber;

}
