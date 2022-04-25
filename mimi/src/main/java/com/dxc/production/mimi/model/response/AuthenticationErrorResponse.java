package com.dxc.production.mimi.model.response;

import org.springframework.http.HttpStatus;

public class AuthenticationErrorResponse extends GenericResponse{
    public AuthenticationErrorResponse(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
