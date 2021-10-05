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
    private String zipCode;

    private String city;

    private String street;

    @JsonProperty("building_number")
    private String buildingNumber;

    public enum CodingKeys {
        zipCode("zip_code"),
        city("city"),
        street("street"),
        buildingNumber("building_number");

        private final String jsonProperty;

        CodingKeys(String jsonProperty) {
            this.jsonProperty = jsonProperty;
        }

        public String getJsonProperty() {
            return this.jsonProperty;
        }

        public static LocationDTO.CodingKeys fromJsonProperty(String jsonProperty) {
            for (LocationDTO.CodingKeys k : LocationDTO.CodingKeys.values()) {
                if (k.jsonProperty.equalsIgnoreCase(jsonProperty)) {
                    return k;
                }
            }
            throw new IllegalArgumentException("No constant with jsonProperty " + jsonProperty + " found");
        }

    }

}
