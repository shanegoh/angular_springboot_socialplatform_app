package com.dxc.production.mimi.service;

import com.dxc.production.mimi.dto.UserDTO;
import com.dxc.production.mimi.model.request.AuthenticationRequest;
import com.dxc.production.mimi.model.request.RegistrationRequest;
import com.dxc.production.mimi.model.response.GenericResponse;

public interface UserServiceInterface {
    UserDTO getUserInformation();

    GenericResponse getJwt(AuthenticationRequest authenticationRequest) throws Exception;

    GenericResponse registerUser(RegistrationRequest registrationRequest);

    GenericResponse updateAccountStatusById(Long id, String username, Integer status);

    GenericResponse getAllUserAccount(Integer pageNumber);
}
