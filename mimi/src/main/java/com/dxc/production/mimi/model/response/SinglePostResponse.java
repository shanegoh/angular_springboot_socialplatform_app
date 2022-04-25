package com.dxc.production.mimi.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SinglePostResponse<T> extends GenericResponse{

    private T postObject;

    public SinglePostResponse(String message, HttpStatus httpStatus, T postObject) {
        super(message, httpStatus);
        this.postObject = postObject;
    }
}
