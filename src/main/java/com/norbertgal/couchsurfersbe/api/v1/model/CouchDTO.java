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
    private Integer numberOfGuests;

    @JsonProperty("number_of_rooms")
    private Integer numberOfRooms;

    private String about;

    private String amenities;

    private Double price;

    private LocationDTO location;

    @JsonProperty("couch_photos")
    private List<CouchPhotoDTO> couchPhotos;

    public enum CodingKeys {
        id("id"),
        name("name"),
        numberOfGuests("number_of_guests"),
        numberOfRooms("number_of_rooms"),
        about("about"),
        amenities("amenities"),
        price("price"),
        location("location");

        private final String jsonProperty;

        CodingKeys(String jsonProperty) {
            this.jsonProperty = jsonProperty;
        }

        public String getJsonProperty() {
            return this.jsonProperty;
        }

        public static CodingKeys fromJsonProperty(String jsonProperty) {
            for (CodingKeys k : CodingKeys.values()) {
                if (k.jsonProperty.equalsIgnoreCase(jsonProperty)) {
                    return k;
                }
            }
            throw new IllegalArgumentException("No constant with jsonProperty " + jsonProperty + " found");
        }

    }
}
