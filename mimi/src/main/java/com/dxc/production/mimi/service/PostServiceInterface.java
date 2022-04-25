package com.dxc.production.mimi.service;

import com.dxc.production.mimi.model.PostEntity;
import com.dxc.production.mimi.model.request.PostRequest;
import com.dxc.production.mimi.model.response.GenericResponse;

import java.util.List;

public interface PostServiceInterface {

    GenericResponse getAllActivePost(int pageNumber);

    GenericResponse postContent(PostRequest postRequest);

    GenericResponse getAllPost(int pageNumber);
}
