package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.dto.UserDto;
import com.dxc.production.mimi.model.response.GenericResponseInterface;
import com.dxc.production.mimi.model.response.UserDetailsResponse;
import com.dxc.production.mimi.service.PostService;
import com.dxc.production.mimi.service.PostServiceInterface;
import com.dxc.production.mimi.service.UserServiceInterface;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST API
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private PostServiceInterface postServiceInterface;

    @GetMapping("/getAllPost/page/{id}")
    public ResponseEntity getAllPost(@PathVariable("id") int pageNumber) {
        GenericResponseInterface genericResponseInterface = postServiceInterface.getAllPost(pageNumber);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }
}
