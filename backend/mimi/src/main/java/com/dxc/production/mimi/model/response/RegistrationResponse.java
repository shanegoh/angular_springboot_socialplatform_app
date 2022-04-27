package com.dxc.production.mimi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RegistrationResponse<T> extends GenericResponse{

    private T errorMessages;
    public RegistrationResponse(String message, HttpStatus httpStatus, T errorMessages) {
        super(message, httpStatus);
        this.errorMessages = errorMessages;
    }
}
