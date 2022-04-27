package com.dxc.production.mimi.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private int role;
}