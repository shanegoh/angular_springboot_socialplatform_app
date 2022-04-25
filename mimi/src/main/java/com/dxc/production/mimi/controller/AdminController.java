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
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private PostServiceInterface postServiceInterface;

    @GetMapping("/getAllPost/page/{page-number}")
    public ResponseEntity getAllPost(@PathVariable("page-number") Integer pageNumber) {
        GenericResponseInterface genericResponseInterface = postServiceInterface.getAllPost(pageNumber);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    @PutMapping("/updatePost/{id}")
    public ResponseEntity updatePost(@PathVariable("id") Long id, @RequestBody PostRequest postRequest) {
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

    @PutMapping("/updateAccountStatus/{id}/{status}")
    public ResponseEntity updateAccountStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        String username = userServiceInterface.getUserInformation().getUsername();
        GenericResponseInterface genericResponseInterface = userServiceInterface
                .updateAccountStatusById(id, username, status);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

}
