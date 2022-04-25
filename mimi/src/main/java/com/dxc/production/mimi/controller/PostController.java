package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.model.request.PostRequest;
import com.dxc.production.mimi.model.response.GenericResponseInterface;
import com.dxc.production.mimi.service.PostServiceInterface;
import com.dxc.production.mimi.service.UserServiceInterface;
import org.apache.coyote.Response;
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
    public ResponseEntity getAllPost(@PathVariable("id") Integer id) {
        GenericResponseInterface genericResponseInterface = postServiceInterface.getAllActivePost(id);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    @PutMapping("/postContent")
    public ResponseEntity postContent(@RequestBody PostRequest postRequest) {
        // Set username and name
        postRequest.setUsername(userServiceInterface.getUserInformation().getUsername());
        postRequest.setName(userServiceInterface.getUserInformation().getName());
        GenericResponseInterface genericResponseInterface = postServiceInterface.postContent(postRequest);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    @PutMapping("/updatePost/{id}")
    public ResponseEntity updatePost(@PathVariable("id") Long id, @RequestBody PostRequest postRequest) {
        // Set username
        postRequest.setUsername(userServiceInterface.getUserInformation().getUsername());
        Integer role = userServiceInterface.getUserInformation().getRole();
        GenericResponseInterface genericResponseInterface = postServiceInterface.updatePost(postRequest, role);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity deletePost(@PathVariable("id") Long id) {
        String username = userServiceInterface.getUserInformation().getUsername();
        Integer role = userServiceInterface.getUserInformation().getRole();
        GenericResponseInterface genericResponseInterface = postServiceInterface.deletePostById(id, username, role);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    @GetMapping("/getPost/{id}")
    public ResponseEntity getPost(@PathVariable("id") Long id) {
        GenericResponseInterface genericResponseInterface = postServiceInterface.getPostById(id);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }
}