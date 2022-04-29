package com.dxc.production.mimi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class MediaResponse extends GenericResponse{
    private byte[] file;
    private String format;

    public MediaResponse(String message, HttpStatus httpStatus, byte[] file, String format) {
        super(message, httpStatus);
        this.file = file;
        this.format = format;
    }
}
