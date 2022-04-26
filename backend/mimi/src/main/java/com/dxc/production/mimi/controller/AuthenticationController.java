package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.model.request.AuthenticationRequest;
import com.dxc.production.mimi.model.response.GenericResponseInterface;
import com.dxc.production.mimi.model.response.UserLogoutResponse;
import com.dxc.production.mimi.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// REST API
@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @PostMapping("/authenticate")
    public ResponseEntity authentication(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        GenericResponseInterface genericResponseInterface = userServiceInterface.getJwt(authenticationRequest);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    @GetMapping("/logout")
    public ResponseEntity attemptLogout (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return new ResponseEntity<>(new UserLogoutResponse(true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new UserLogoutResponse(false), HttpStatus.OK);
        }
    }
}

