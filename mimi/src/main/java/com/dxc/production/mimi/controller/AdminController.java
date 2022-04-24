package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.dto.UserDto;
import com.dxc.production.mimi.model.response.UserDetailsResponse;
import com.dxc.production.mimi.service.UserServiceInterface;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST API
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @GetMapping("/getAdminInformation")
    public ResponseEntity getAdminInformation() {
        UserDto userDto = userServiceInterface.getUserInformation();
        UserDetailsResponse returnValue = new UserDetailsResponse();
        BeanUtils.copyProperties(userDto, returnValue);
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }
}
