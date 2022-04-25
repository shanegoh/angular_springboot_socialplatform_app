package com.dxc.production.mimi.service;

import com.dxc.production.mimi.model.request.PostRequest;
import com.dxc.production.mimi.model.response.GenericResponse;
import net.bytebuddy.description.type.TypeList;

public interface PostServiceInterface {

    GenericResponse getAllActivePost(Integer pageNumber);

    GenericResponse postContent(PostRequest postRequest);

    GenericResponse getAllPost(Integer pageNumber);

    GenericResponse updatePost(PostRequest postRequest, Integer role);

    GenericResponse deletePostById(Long id, String username, Integer role);

    GenericResponse getPostById(Long id);
}
