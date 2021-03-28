package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouchDTO {

    private Long id;

    private String name;

    @JsonProperty("number_of_guests")
    private int numberOfGuests;

    @JsonProperty("number_of_rooms")
    private int numberOfRooms;

    private String about;

    private String amenities;

    private Double price;

    private LocationDTO location;

    @JsonProperty("couch_photos")
    private List<CouchPhotoDTO> couchPhotos;

}
