package com.dxc.production.mimi.model.response;

import org.springframework.http.HttpStatus;

public interface GenericResponseInterface {
    public String getMessage();
    public HttpStatus getHttpStatus();
}
