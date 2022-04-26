package com.dxc.production.mimi.service;

import com.dxc.production.mimi.dao.PostRepo;
import com.dxc.production.mimi.dto.PostDTO;
import com.dxc.production.mimi.enumerate.Media;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PostService implements PostServiceInterface {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private FileStorageServiceInterface fileStorageServiceInterface;

    @Override
    public GenericResponse getAllActivePost(Integer pageNumber) {
        List<PostDTO> postDTOList = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10); // Set page size
            Page<PostEntity> postEntityList = postRepo.findAllActivePost(pageable); // update created and last modified by
            if(postEntityList.isEmpty())
                return new GenericResponse("No more records.", HttpStatus.OK);
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
    public GenericResponse postContent(MultipartFile file, PostRequest postRequest) {
        PostEntity postEntity = new PostEntity();
        // Check if object is null or not
        if(Objects.nonNull(file)) {
            StringBuilder filePath = new StringBuilder();
            try {   // try save
                filePath.append(fileStorageServiceInterface.save(file, getContentType(file).toString()));
                postEntity.setMediaLink(filePath.toString()); // set media
            } catch (Exception e) {
                return new GenericResponse("Could not upload the file: " + file.getOriginalFilename(),
                        HttpStatus.OK);
            }
        }
        try {
            postEntity.setCaption(postRequest.getCaption());
            postEntity.setHyperLink(postRequest.getHyperLink());
            postEntity.setUsername(postRequest.getUsername());
            postEntity.setName(postRequest.getName());
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
            if(postEntityList.isEmpty())
                return new GenericResponse("No more records.", HttpStatus.OK);

            return new PostResponse<>("Successfully retrieve all record(s).", HttpStatus.OK, postEntityList);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new GenericResponse("Unable to load record(s).", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GenericResponse updatePost(MultipartFile file, PostRequest postRequest, Integer role) {
        try {
            PostEntity postEntity = postRepo.findById(postRequest.getId());  // Find if the post exist
            // if role is user, check eligibility to update.
            if (role == Role.USER.getValue())
                if (!postEntity.getUsername().equals(postRequest.getUsername())) {
                    return new GenericResponse("Error. You are not allowed to update this post.",
                            HttpStatus.UNAUTHORIZED);
                }
            // Check if object is null or not
            if(Objects.nonNull(file)) {
                StringBuilder filePath = new StringBuilder();
                try {
                    filePath.append(fileStorageServiceInterface.save(file, getContentType(file).toString()));
                    postEntity.setMediaLink(filePath.toString()); // set media
                } catch (Exception e) {
                    return new GenericResponse("Could not upload the file: " + file.getOriginalFilename(),
                            HttpStatus.OK);
                }
            }
            // Update all fields
            postEntity.setCaption(postRequest.getCaption());        // set caption
            postEntity.setHyperLink(postRequest.getHyperLink());    // set hyper link
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new GenericResponse("Error. Failed to delete post.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GenericResponse getPostById(Long id) {
        try {
            PostEntity postEntity = postRepo.findActivePostById(id);
            postEntity.setViewCount(postEntity.getViewCount() + 1); // increment
            postRepo.save(postEntity); // try save
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(postEntity, postDTO);
            return new SinglePostResponse<>("Successfully retrieve record.", HttpStatus.OK, postDTO);
        } catch (Exception e) {
            return new GenericResponse("Error. Failed to retrieve record.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method
    private StringBuilder getContentType(MultipartFile file) {
        final String PNG = Media.PNG.getFormat();
        final String JPEG = Media.JPEG.getFormat();
        final String JPG = Media.JPG.getFormat();
        final String MP4 = Media.MP4.getFormat();
        StringBuilder type = new StringBuilder();

        switch (Media.formatToMedia(file.getContentType())) {
            case PNG : type.append(Media.PNG.getType());
                break;
            case JPEG: type.append(Media.JPEG.getType());
                break;
            case JPG: type.append(Media.JPG.getType());
                break;
            case MP4: type.append(Media.MP4.getType());
                break;
            default:
                System.err.println("No Such Content Type.");
        }
        return type;
    }
}
