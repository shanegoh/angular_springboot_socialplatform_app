package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.model.request.PostRequest;
import com.dxc.production.mimi.model.response.GenericResponseInterface;
import com.dxc.production.mimi.service.PostServiceInterface;
import com.dxc.production.mimi.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST API
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/user")
public class PostController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private PostServiceInterface postServiceInterface;

    @GetMapping("/getAllPost/page/{id}")
    public ResponseEntity getAllPost(@PathVariable("id") int id) {

        GenericResponseInterface genericResponseInterface = postServiceInterface.getAllActivePost(id);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    @PostMapping("/postContent")
    public ResponseEntity postContent(@RequestBody PostRequest postRequest) {
        // Set username and name
        postRequest.setUsername(userServiceInterface.getUserInformation().getUsername());
        postRequest.setName(userServiceInterface.getUserInformation().getName());
        GenericResponseInterface genericResponseInterface = postServiceInterface.postContent(postRequest);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }
}