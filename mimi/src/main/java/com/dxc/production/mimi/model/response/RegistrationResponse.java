package com.dxc.production.mimi.model.response;

import lombok.Data;
import org.springframework.http.HttpStatus;



public class RegistrationResponse extends GenericResponse {

    public RegistrationResponse(String message, HttpStatus httpStatus) {
        super(message,httpStatus);
    }
}
