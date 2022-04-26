package com.dxc.production.mimi.model.response;

import org.springframework.http.HttpStatus;

public class AuthenticationResponse extends GenericResponse{
    private String jsonWebToken;

    public AuthenticationResponse(String message, HttpStatus httpStatus, String jsonWebToken) {
        super(message, httpStatus);
        this.jsonWebToken = jsonWebToken;
    }

    public String getJsonWebToken() {
        return "Bearer " + jsonWebToken;
    }
}
