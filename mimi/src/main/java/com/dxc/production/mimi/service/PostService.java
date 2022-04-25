package com.dxc.production.mimi.service;

import com.dxc.production.mimi.dao.PostRepo;
import com.dxc.production.mimi.dto.PostDTO;
import com.dxc.production.mimi.enumerate.Role;
import com.dxc.production.mimi.enumerate.Status;
import com.dxc.production.mimi.model.PostEntity;
import com.dxc.production.mimi.model.request.PostRequest;
import com.dxc.production.mimi.model.response.GenericResponse;
import com.dxc.production.mimi.model.response.PostResponse;
import com.dxc.production.mimi.model.response.SinglePostResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService implements PostServiceInterface {

    @Autowired
    private PostRepo postRepo;

    @Override
    public GenericResponse getAllActivePost(Integer pageNumber) {
        List<PostDTO> postDTOList = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10); // Set page size
            Page<PostEntity> postEntityList = postRepo.findAllActivePost(pageable); // update created and last modified by
            // Copy to PostDTO
            for (PostEntity postEntity : postEntityList) {
                PostDTO postDto = new PostDTO();
                BeanUtils.copyProperties(postEntity, postDto);
                postDTOList.add(postDto);
            }
            return new PostResponse<>("Successfully retrieve all record(s).",
                    HttpStatus.OK, new PageImpl<PostDTO>(postDTOList, pageable, postEntityList.getTotalElements()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new GenericResponse("Unable to load record(s).", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GenericResponse postContent(PostRequest postRequest) {
        try {
            PostEntity postEntity = new PostEntity();
            BeanUtils.copyProperties(postRequest, postEntity);   // copy over postRequest details
            postEntity.setCreatedBy(postRequest.getUsername());  // update created and last modified by
            postEntity.setLastModifiedBy(postRequest.getUsername()); // update modified by username
            postRepo.save(postEntity);   // try save
        } catch (Exception e) {
            return new GenericResponse("Failed to post. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new GenericResponse("Content Posted.", HttpStatus.CREATED);
    }

    @Override
    public GenericResponse getAllPost(Integer pageNumber) {
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10); // Set page size
            Page<PostEntity> postEntityList = postRepo.findAll(pageable);       // Retrieve records for certain page
            return new PostResponse<>("Successfully retrieve all record(s).", HttpStatus.OK, postEntityList);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new GenericResponse("Unable to load record(s).", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GenericResponse updatePost(PostRequest postRequest, Integer role) {
        try {
            PostEntity postEntity = postRepo.findById(postRequest.getId());  // Find if the post exist
            // if role is user, check eligibility to update.
            if (role == Role.USER.getValue())
                if (!postEntity.getUsername().equals(postRequest.getUsername())) {
                    return new GenericResponse("Error. You are not allowed to update this post.",
                            HttpStatus.UNAUTHORIZED);
                }
            // Update all fields
            postEntity.setCaption(postRequest.getCaption());        // set caption
            postEntity.setHyperLink(postRequest.getHyperLink());    // set hyper link
            postEntity.setMediaLink(postRequest.getMediaLink());    // set media link
            postEntity.setLastModifiedBy(postRequest.getUsername()); // set modified username
            postRepo.save(postEntity);  // try save

            return new GenericResponse("Successfully updated record.", HttpStatus.OK);
        } catch (Exception e) {
            return new GenericResponse("Error. Failed to update post.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GenericResponse deletePostById(Long id, String username, Integer role) {
        try {
            PostEntity postEntity = postRepo.findById(id.longValue());
            // if role is user, check eligibility to delete.
            if (role == Role.USER.getValue())
                if (!postEntity.getUsername().equals(username)) {
                    return new GenericResponse("Error. You are not allowed to delete this post.",
                            HttpStatus.UNAUTHORIZED);
                }
            postEntity.setDeleteFlag(Status.DELETED.getValue()); // set account status deleted
            postEntity.setLastModifiedBy(username); // set modified username
            postRepo.save(postEntity); // try save

            return new GenericResponse("Successfully deleted record.", HttpStatus.OK);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return new GenericResponse("Error. Failed to delete post.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GenericResponse getPostById(Long id) {
        try {
            PostEntity postEntity = postRepo.findById(id.longValue());
            postEntity.setViewCount(postEntity.getViewCount() + 1); // increment
            postRepo.save(postEntity); // try save
            return new SinglePostResponse<>("Successfully retrieve record.", HttpStatus.OK, postEntity);
        } catch (Exception e) {
            return new GenericResponse("Error. Failed to retrieve record.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
