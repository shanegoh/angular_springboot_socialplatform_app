package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.model.request.PostRequest;
import com.dxc.production.mimi.model.response.GenericResponseInterface;
import com.dxc.production.mimi.service.PostServiceInterface;
import com.dxc.production.mimi.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// REST API
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/admin")
public class AdminController {

//    @Autowired
//    private UserServiceInterface userServiceInterface;
//
//    @Autowired
//    private PostServiceInterface postServiceInterface;
//
//    // Admin
//    @GetMapping("/getAllPost/page/{page-number}")
//    public ResponseEntity getAllPost(@PathVariable("page-number") Integer pageNumber) {
//        GenericResponseInterface genericResponseInterface = postServiceInterface.getAllPost(pageNumber);
//        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
//    }
//
//    // Share
//    @PutMapping("/updatePost")
//    public ResponseEntity updatePost(@RequestPart("media") MultipartFile multipartFile, @RequestBody PostRequest postRequest) {
//        postRequest.setUsername(userServiceInterface.getUserInformation().getUsername());
//        Integer role = userServiceInterface.getUserInformation().getRole();
//        GenericResponseInterface genericResponseInterface = postServiceInterface.updatePost(multipartFile, postRequest, role);
//        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
//    }
//
//    // Share
//    @DeleteMapping("/deletePost/{id}")
//    public ResponseEntity deletePost(@PathVariable("id") Long id) {
//        String username = userServiceInterface.getUserInformation().getUsername();
//        Integer role = userServiceInterface.getUserInformation().getRole();
//        GenericResponseInterface genericResponseInterface = postServiceInterface.deletePostById(id, username, role);
//        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
//    }
//
//    // Admin
//    @PutMapping("/updateAccountStatus/{id}/{status}")
//    public ResponseEntity updateAccountStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
//        String username = userServiceInterface.getUserInformation().getUsername();
//        GenericResponseInterface genericResponseInterface = userServiceInterface
//                .updateAccountStatusById(id, username, status);
//        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
//    }
}
