package com.dxc.production.mimi.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {
    private String username;
    private String name;
    private int role;
}
