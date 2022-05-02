package com.dxc.production.mimi.service;

import com.dxc.production.mimi.dao.PostRepo;
import com.dxc.production.mimi.dto.AccountDTO;
import com.dxc.production.mimi.dto.PostDTO;
import com.dxc.production.mimi.dto.RegistrationErrorDTO;
import com.dxc.production.mimi.enumerate.Message;
import com.dxc.production.mimi.enumerate.Role;
import com.dxc.production.mimi.enumerate.Status;
import com.dxc.production.mimi.model.PostEntity;
import com.dxc.production.mimi.model.request.RegistrationRequest;
import com.dxc.production.mimi.model.response.*;
import com.dxc.production.mimi.securityconfig.CustomUserDetailsService;
import com.dxc.production.mimi.securityconfig.JwtUtil;
import com.dxc.production.mimi.dao.UserRepo;
import com.dxc.production.mimi.dto.UserDTO;
import com.dxc.production.mimi.model.UserEntity;
import com.dxc.production.mimi.model.request.AuthenticationRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService implements UserServiceInterface {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private PostRepo postRepo;

    @Override
    public UserDTO getUserInformation() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        UserEntity userEntity = userRepo.findByUsername(userDetails.getUsername()); // Using the name save in context
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public GenericResponse getJwt(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            // Since we are using username and password we then use UsernamePassword authentication token
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            return new AuthenticationErrorResponse("Account has been disabled. Please contact administrator.",
                    HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException e) {
            return new AuthenticationErrorResponse("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
        // Verify the existence of the user from database
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse("Successfully retrieve token.", HttpStatus.OK, token);
    }

    @Override
    public GenericResponse registerUser(RegistrationRequest registrationRequest) {
        // Pull from database and check if the username exist of not
        UserEntity checkUsernameExist = userRepo.findByUsername(registrationRequest.getUsername().trim().toLowerCase());
        UserEntity checkEmailExist = userRepo.findByEmail(registrationRequest.getEmail().trim().toLowerCase());
        RegistrationErrorDTO registrationErrorDTO = new RegistrationErrorDTO();

        if (Objects.nonNull(checkUsernameExist) && Objects.nonNull(checkEmailExist)) {
            registrationErrorDTO.setUsernameErrorMsg(Message.USERNAME_EXIST.getMessage());
            registrationErrorDTO.setEmailErrorMsg(Message.EMAIL_EXIST.getMessage());
            return new RegistrationResponse("Username and email exist, please try another.", HttpStatus.CONFLICT, registrationErrorDTO);
        } else if (Objects.nonNull(checkUsernameExist)) {  // Show response when username is exist
            registrationErrorDTO.setUsernameErrorMsg(Message.USERNAME_EXIST.getMessage());
            return new RegistrationResponse("Username exist, please try another.", HttpStatus.CONFLICT, registrationErrorDTO);
        } else if (Objects.nonNull(checkEmailExist)) {  // Show response when email is exist
            registrationErrorDTO.setEmailErrorMsg(Message.EMAIL_EXIST.getMessage());
            return new RegistrationResponse("Email exist, please try another.", HttpStatus.CONFLICT, registrationErrorDTO);
        }
        // Start to validate all information
        boolean registrationResult = validateRegistrationUser(registrationRequest, registrationErrorDTO);

        // If validation success, proceed to add
        if (registrationResult) {
            try {
                UserEntity newUser = new UserEntity();
                newUser.setUsername(registrationRequest.getUsername().toLowerCase());
                newUser.setName(registrationRequest.getName());
                newUser.setRole(Role.USER.getValue());
                newUser.setEmail(registrationRequest.getEmail());
                newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                newUser.setCreatedBy(registrationRequest.getUsername().toLowerCase());
                newUser.setLastModifiedBy(registrationRequest.getUsername().toLowerCase());
                userRepo.save(newUser);
                return new GenericResponse("Successfully created user.", HttpStatus.CREATED);
            } catch (Exception e) {
                return new GenericResponse("Unable to create. Please try again later.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new RegistrationResponse("Multiple Invalid Fields", HttpStatus.CONFLICT, registrationErrorDTO);
        }
    }

    @Override
    public GenericResponse updateAccountStatusById(Long id, String username, Integer status) {
        try {
            postRepo.updatePostStatusByUsername(username, status);
            UserEntity userEntity = userRepo.findById(id.longValue());
            userEntity.setDeleteFlag(Status.valueToRole(status).getValue()); // update to active account
            userEntity.setLastModifiedBy(username); // set modified username
            userRepo.save(userEntity); // try save

            return new GenericResponse("Successfully updated user status.", HttpStatus.OK);
        } catch (Exception e) {
            return new GenericResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GenericResponse getAllUserAccount(Integer pageNumber) {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10); // Set page size
            Page<UserEntity> userEntityList = userRepo.findAll(pageable);       // Retrieve records for certain page
            if (userEntityList.isEmpty())
                return new GenericResponse("No more records.", HttpStatus.NOT_FOUND);

            // Copy to PostDTO
            for (UserEntity userEntity : userEntityList) {
                AccountDTO accountDTO = new AccountDTO();
                BeanUtils.copyProperties(userEntity, accountDTO);
                accountDTOList.add(accountDTO);
            }
            return new PostResponse<>("Successfully retrieve all record(s).",
                    HttpStatus.OK, new PageImpl<AccountDTO>(accountDTOList, pageable, userEntityList.getTotalElements()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new GenericResponse("Unable to load record(s).", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GenericResponse searchAccountByKeyword(Integer pageNumber, String searchText) {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber - 1, 10); // Set page size
            Page<UserEntity> userEntityList = userRepo.searchAccountByKeyword(pageable, searchText);       // Retrieve records for certain page
            if (userEntityList.isEmpty())
                return new GenericResponse("No more records.", HttpStatus.NOT_FOUND);

            // Copy to PostDTO
            for (UserEntity userEntity : userEntityList) {
                AccountDTO accountDTO = new AccountDTO();
                BeanUtils.copyProperties(userEntity, accountDTO);
                accountDTOList.add(accountDTO);
            }
            return new PostResponse<>("Successfully retrieve all record(s).",
                    HttpStatus.OK, new PageImpl<AccountDTO>(accountDTOList, pageable, userEntityList.getTotalElements()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new GenericResponse("Unable to load record(s).", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to do validation
    private boolean validateRegistrationUser(RegistrationRequest request, RegistrationErrorDTO registrationErrorDTO) {
        // Check username, name, role and password
        if (StringUtils.hasText(request.getUsername())
                && StringUtils.hasText(request.getName())
                && StringUtils.hasText(request.getPassword())
                && StringUtils.hasText(request.getEmail())) {

            // Only allow digit, underscore and alphabets
            boolean validateUsername = Pattern.matches("[\\w]+", request.getUsername());

            // Only allow alphabets
            boolean validateName = Pattern.matches("[A-Za-z ]+", request.getName());

            // Should include 1 lower case & 1 upper case alphabet,
            // 1 digit, and at least 1 of (@,#,$,%) -> Min length of 8, Max 16
            boolean validatePassword = Pattern.matches("^(?=(.*[0-9])+)(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[@#$%])+).{8,16}$", request.getPassword());

            boolean validateEmail = Pattern.matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", request.getEmail());

            if (!validateUsername) {
                registrationErrorDTO.setUsernameErrorMsg(Message.USERNAME_INVALID.getMessage());
            }

            if (!validateName) {
                registrationErrorDTO.setNameErrorMsg(Message.NAME_INVALID.getMessage());
            }

            if (!validatePassword) {
                registrationErrorDTO.setPasswordErrorMsg(Message.PASSWORD_INVALID.getMessage());
            }

            if (!validateEmail) {
                registrationErrorDTO.setEmailErrorMsg(Message.EMAIL_INVALID.getMessage());
            }

            return (validateUsername && validateName && validatePassword);
        }
        return false;
    }
}
