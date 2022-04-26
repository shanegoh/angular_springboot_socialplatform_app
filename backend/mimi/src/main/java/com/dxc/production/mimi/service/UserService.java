package com.dxc.production.mimi.service;

import com.dxc.production.mimi.enumerate.Status;
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
        UserEntity userEntity = userRepo.findByUsername(registrationRequest.getUsername().trim().toLowerCase());
        // Show response when username is found
        if (Objects.nonNull(userEntity)) {
            return new GenericResponse("Username exist, please try another.", HttpStatus.CONFLICT);
        }

        // Start to validate all information
        StringBuilder errorMessage = new StringBuilder();
        boolean registrationResult = validateRegistrationUser(registrationRequest, errorMessage);

        // If validation success, proceed to add
        if (registrationResult) {
            try {
                UserEntity newUser = new UserEntity();
                newUser.setUsername(registrationRequest.getUsername().toLowerCase());
                newUser.setName(registrationRequest.getName());
                newUser.setRole(registrationRequest.getRole());
                newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                newUser.setCreatedBy(registrationRequest.getUsername().toLowerCase());
                newUser.setLastModifiedBy(registrationRequest.getUsername().toLowerCase());
                userRepo.save(newUser);
                return new GenericResponse("Successfully created user.", HttpStatus.CREATED);
            } catch (Exception e) {
                return new GenericResponse("Unable to create. Please try again later.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new GenericResponse(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public GenericResponse updateAccountStatusById(Long id, String username, Integer status) {
        try {
            UserEntity userEntity = userRepo.findById(id.longValue());
            userEntity.setDeleteFlag(Status.valueToRole(status).getValue()); // update to active account
            userEntity.setLastModifiedBy(username); // set modified username
            userRepo.save(userEntity); // try save

            return new GenericResponse("Successfully updated user status.", HttpStatus.OK);
        } catch (Exception e) {
            return new GenericResponse("Error. Unable to update user status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to do validation
    private boolean validateRegistrationUser(RegistrationRequest request, StringBuilder errorMessage) {
        // Check username, name, role and password
        if (StringUtils.hasText(request.getUsername())
                && StringUtils.hasText(request.getName())
                && (request.getRole() == 0 || request.getRole() == 1)
                && StringUtils.hasText(request.getPassword())) {

            // Only allow digit, underscore and alphabets
            boolean validateUsername = Pattern.matches("[\\w]+", request.getUsername());

            // Only allow alphabets
            boolean validateName = Pattern.matches("[A-Za-z ]+", request.getName());

            // Should include 1 lower case & 1 upper case alphabet,
            // 1 digit, and at least 1 of (@,#,$,%) -> Min length of 8, Max 16
            boolean validatePassword = Pattern.matches("^(?=(.*[0-9])+)"
                                        + "(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[@#$%])+).{8,16}$", request.getPassword());
            if (!validateUsername) {
                errorMessage.append("Invalid Username: Only digit, underscore and alphabets allowed. ");
            }

            if (!validateName) {
                errorMessage.append("Invalid Name: Only alphabets allowed. ");
            }

            if (!validatePassword) {
                errorMessage.append("Invalid Password: Must include at least 1 lower case and 1 upper case alphabet, "
                        + "1 digit and 1 of special characters (@#$%). ");
            }
            return (validateUsername && validateName && validatePassword);
        } else {
            errorMessage.append("Error. Please ensure that all fields are filled up.");
        }
        return false;
    }
}
