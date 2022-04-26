package com.dxc.production.mimi.service;

import com.dxc.production.mimi.model.request.PostRequest;
import com.dxc.production.mimi.model.response.GenericResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostServiceInterface {

    GenericResponse getAllActivePost(Integer pageNumber);

    GenericResponse postContent(MultipartFile multipartFile, PostRequest postRequest);

    GenericResponse getAllPost(Integer pageNumber);

    GenericResponse updatePost(MultipartFile multipartFile, PostRequest postRequest, Integer role);

    GenericResponse deletePostById(Long id, String username, Integer role);

    GenericResponse getPostById(Long id);
}
