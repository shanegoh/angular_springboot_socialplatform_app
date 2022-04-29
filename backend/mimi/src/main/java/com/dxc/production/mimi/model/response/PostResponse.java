package com.dxc.production.mimi.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PostResponse<T> extends GenericResponse{

    private Page<T> pagination;

    public PostResponse(String message, HttpStatus httpStatus, Page<T> pagination) {
        super(message, httpStatus);
        this.pagination = pagination;
    }
}
