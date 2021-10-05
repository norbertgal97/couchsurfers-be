package com.norbertgal.couchsurfersbe.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDTO {
    private Long id;
    private Integer size;
    private String name;
}
