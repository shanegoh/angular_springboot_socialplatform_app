package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.model.request.RegistrationRequest;
import com.dxc.production.mimi.model.response.GenericResponseInterface;
import com.dxc.production.mimi.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @PostMapping("/register")
    public ResponseEntity registration(@RequestBody RegistrationRequest registrationRequest) throws Exception {
        GenericResponseInterface genericResponseInterface = userServiceInterface.registerUser(registrationRequest);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }
}
