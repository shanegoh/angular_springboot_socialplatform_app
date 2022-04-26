package com.dxc.production.mimi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLogoutResponse {
    private boolean isLoggedOut;
}
