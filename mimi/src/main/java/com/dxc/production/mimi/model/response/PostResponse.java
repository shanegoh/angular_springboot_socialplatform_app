package com.dxc.production.mimi.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import java.util.List;

@Getter
@Setter
public class PostResponse<T> extends GenericResponse{

    private Page<T> postList;

    public PostResponse(String message, HttpStatus httpStatus, Page<T> postList) {
        super(message, httpStatus);
        this.postList = postList;
    }
}
