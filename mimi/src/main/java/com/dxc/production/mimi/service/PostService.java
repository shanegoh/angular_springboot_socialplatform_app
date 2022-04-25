package com.dxc.production.mimi.service;

import com.dxc.production.mimi.dao.PostRepo;
import com.dxc.production.mimi.dto.PostDto;
import com.dxc.production.mimi.model.PostEntity;
import com.dxc.production.mimi.model.request.PostRequest;
import com.dxc.production.mimi.model.response.GenericResponse;
import com.dxc.production.mimi.model.response.PostResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService implements PostServiceInterface {

    @Autowired
    private PostRepo postRepo;

    @Override
    public GenericResponse getAllActivePost(int pageNumber) {
        List<PostDto> postDtoList = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10);
            Page<PostEntity> postEntityList = postRepo.findAllActivePost(pageable);
            System.err.println(postEntityList.getTotalElements());
            System.err.println(postEntityList.getTotalPages());
            // Copy to another bean
            for (PostEntity postEntity : postEntityList) {
                PostDto postDto = new PostDto();
                BeanUtils.copyProperties(postEntity, postDto);
                postDtoList.add(postDto);
            }
            System.err.println(postDtoList.size());
            return new PostResponse<>("Successfully retrieve all record(s).",
                    HttpStatus.OK, new PageImpl<PostDto>(postDtoList, pageable, postEntityList.getTotalElements()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new GenericResponse("Unable to load record(s).", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GenericResponse postContent(PostRequest postRequest) {
        PostEntity postEntity = new PostEntity();
        BeanUtils.copyProperties(postRequest, postEntity);   // copy over post details
        postEntity.setCreatedBy(postRequest.getUsername());  // update created and last modified by
        postEntity.setLastModifiedBy(postRequest.getUsername());

        try {
            postRepo.save(postEntity);   // try save
        } catch (Exception e) {
            return new GenericResponse("Failed to post. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new GenericResponse("Content Posted.", HttpStatus.CREATED);
    }

    @Override
    public GenericResponse getAllPost(int pageNumber) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10);
            Page<PostEntity> postEntityList = postRepo.findAll(pageable);
            return new PostResponse<>("Successfully retrieve all record(s).", HttpStatus.OK, postEntityList);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new GenericResponse("Unable to load record(s).", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
