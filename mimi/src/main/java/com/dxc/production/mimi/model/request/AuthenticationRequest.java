package com.dxc.production.mimi.model.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
