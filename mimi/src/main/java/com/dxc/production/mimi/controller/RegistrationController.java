package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.dto.UserDto;
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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
        GenericResponseInterface genericResponseInterface = userServiceInterface.registerUser(user);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }
}
