package com.norbertgal.couchsurfersbe.api.v1.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostRequestDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("couch_id")
    private Long couchId;
}
