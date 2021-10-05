package com.norbertgal.couchsurfersbe.api.v1.model.exception;

import com.norbertgal.couchsurfersbe.api.v1.model.StatusDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IOException extends Exception {
    StatusDTO status = new StatusDTO();

    @Override
    public String getMessage() {
        return status.getErrorMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return status.getErrorMessage();
    }
}