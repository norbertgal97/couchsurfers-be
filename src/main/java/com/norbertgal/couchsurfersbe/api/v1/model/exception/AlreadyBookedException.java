package com.norbertgal.couchsurfersbe.api.v1.model.exception;

import com.norbertgal.couchsurfersbe.api.v1.model.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlreadyBookedException extends Exception {
    ErrorDTO status = new ErrorDTO();

    @Override
    public String getMessage() {
        return status.getErrorMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return status.getErrorMessage();
    }
}
