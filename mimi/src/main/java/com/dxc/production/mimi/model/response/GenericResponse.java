package com.dxc.production.mimi.model.response;

import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
abstract public class GenericResponse<T> implements GenericResponseInterface{
    protected String message;
    protected HttpStatus httpStatus;

    public GenericResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
