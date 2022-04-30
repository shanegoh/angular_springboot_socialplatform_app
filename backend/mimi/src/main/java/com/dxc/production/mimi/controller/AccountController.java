package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.model.response.GenericResponseInterface;
import com.dxc.production.mimi.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST API
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/admin")
public class AccountController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    // Admin -> API TESTED OK
    @PutMapping("/updateAccountStatus/{id}/{status}")
    public ResponseEntity updateAccountStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        String username = userServiceInterface.getUserInformation().getUsername();
        GenericResponseInterface genericResponseInterface = userServiceInterface
                .updateAccountStatusById(id, username, status);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    @GetMapping("/getAllUserAccount/{page-number}")
    public ResponseEntity getAllUserAccount(@PathVariable("page-number") Integer pageNumber) {
        GenericResponseInterface genericResponseInterface = userServiceInterface.getAllUserAccount(pageNumber);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }
}
