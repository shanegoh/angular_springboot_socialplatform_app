package com.dxc.production.mimi.model.response;

import com.dxc.production.mimi.dto.PostDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Getter
@Setter
public class GenericResponse implements GenericResponseInterface{
    // Time Stamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timeStamp;
    protected String message;
    protected HttpStatus httpStatus;

    public GenericResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timeStamp = LocalDateTime.now();
    }

}
