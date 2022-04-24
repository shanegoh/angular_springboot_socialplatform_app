package com.dxc.production.mimi.service;

import com.dxc.production.mimi.dto.UserDto;
import com.dxc.production.mimi.model.request.AuthenticationRequest;
import com.dxc.production.mimi.model.response.AuthenticationResponse;
import com.dxc.production.mimi.model.response.GenericResponse;
import com.dxc.production.mimi.model.response.RegistrationResponse;

public interface UserServiceInterface {
    UserDto getUserInformation();

    AuthenticationResponse getJwt(AuthenticationRequest authenticationRequest) throws Exception;

    RegistrationResponse registerUser(UserDto user);
}
