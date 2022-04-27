package com.dxc.production.mimi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationErrorDTO {
    private String usernameErrorMsg = "";
    private String emailErrorMsg = "";
    private String nameErrorMsg = "";
    private String passwordErrorMsg = "";
}
