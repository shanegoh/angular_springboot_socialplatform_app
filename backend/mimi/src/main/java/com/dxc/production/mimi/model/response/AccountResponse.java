package com.dxc.production.mimi.model.response;

import com.dxc.production.mimi.dto.AccountDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
public class AccountResponse<T> extends GenericResponse {

    private Page<T> pagination;

    public AccountResponse(String message, HttpStatus httpStatus, Page<T> pagination) {
        super(message, httpStatus);
        this.pagination = pagination;
    }
}
