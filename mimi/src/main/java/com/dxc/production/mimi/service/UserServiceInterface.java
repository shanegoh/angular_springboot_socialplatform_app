package com.dxc.production.mimi.service;

import com.dxc.production.mimi.dto.UserDto;
import com.dxc.production.mimi.model.request.AuthenticationRequest;
import com.dxc.production.mimi.model.request.RegistrationRequest;
import com.dxc.production.mimi.model.response.GenericResponse;

public interface UserServiceInterface {
    UserDto getUserInformation();

    GenericResponse getJwt(AuthenticationRequest authenticationRequest) throws Exception;

    GenericResponse registerUser(RegistrationRequest registrationRequest);
}
