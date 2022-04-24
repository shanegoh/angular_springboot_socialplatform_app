package com.dxc.production.mimi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
