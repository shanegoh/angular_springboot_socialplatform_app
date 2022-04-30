package com.dxc.production.mimi.controller;

import com.dxc.production.mimi.model.request.PostRequest;
import com.dxc.production.mimi.model.response.GenericResponseInterface;
import com.dxc.production.mimi.service.FileStorageServiceInterface;
import com.dxc.production.mimi.service.PostServiceInterface;
import com.dxc.production.mimi.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// REST API
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private PostServiceInterface postServiceInterface;

    @Autowired
    private FileStorageServiceInterface fileStorageServiceInterface;

    // User Only -> API TESTED
    @GetMapping("/user/getAllPost/page/{page-number}")
    public ResponseEntity getAllActivePost(@PathVariable("page-number") Integer pageNumber) {
        GenericResponseInterface genericResponseInterface = postServiceInterface.getAllActivePost(pageNumber);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    // User Only -> API TESTED
    @PostMapping("/user/postContent")
    public ResponseEntity postContent(@RequestPart(value = "media", required = false) MultipartFile multipartFile,
                                      @RequestPart("body") PostRequest postRequest) {
        // Set username and name
        postRequest.setUsername(userServiceInterface.getUserInformation().getUsername());
        postRequest.setName(userServiceInterface.getUserInformation().getName());
        GenericResponseInterface genericResponseInterface = postServiceInterface.postContent(multipartFile, postRequest);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    // Shared -> API TESTED
    @PutMapping("/updatePost")
    public ResponseEntity updatePost(@RequestPart(value = "media", required = false) MultipartFile multipartFile,
                                     @RequestPart("body") PostRequest postRequest) {
        System.out.println(postRequest.getHyperLink());
        System.out.println(postRequest.getCaption());
        // Set username
        postRequest.setUsername(userServiceInterface.getUserInformation().getUsername());
        Integer role = userServiceInterface.getUserInformation().getRole();
        GenericResponseInterface genericResponseInterface = postServiceInterface.updatePost(multipartFile, postRequest, role);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    // Shared -> API TESTED
    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity deletePost(@PathVariable("id") Long id) {
        String username = userServiceInterface.getUserInformation().getUsername();
        Integer role = userServiceInterface.getUserInformation().getRole();
        GenericResponseInterface genericResponseInterface = postServiceInterface.deletePostById(id, username, role);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    // Shared -> API TESTED
    @GetMapping("/getPost/{id}")
    public ResponseEntity getPost(@PathVariable("id") Long id) {
        Integer role = userServiceInterface.getUserInformation().getRole();
        GenericResponseInterface genericResponseInterface = postServiceInterface.getPostById(id);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    // Admin -> API TESTED OK
    @GetMapping("/admin/getAllPost/page/{page-number}")
    public ResponseEntity getAllPost(@PathVariable("page-number") Integer pageNumber) {
        GenericResponseInterface genericResponseInterface = postServiceInterface.getAllPost(pageNumber);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }

    // USER -> API TEST OK
    @GetMapping("/stream/uploads/{url}")
    public ResponseEntity stream(@PathVariable("url") String url) throws IOException {
        GenericResponseInterface genericResponseInterface = fileStorageServiceInterface.streamMedia(url);
        return new ResponseEntity<>(genericResponseInterface, genericResponseInterface.getHttpStatus());
    }
}
