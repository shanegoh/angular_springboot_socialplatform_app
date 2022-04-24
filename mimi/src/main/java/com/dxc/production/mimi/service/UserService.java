package com.dxc.production.mimi.service;

import com.dxc.production.mimi.securityconfig.CustomUserDetailsService;
import com.dxc.production.mimi.securityconfig.JwtUtil;
import com.dxc.production.mimi.dao.UserRepo;
import com.dxc.production.mimi.dto.UserDto;
import com.dxc.production.mimi.model.UserEntity;
import com.dxc.production.mimi.model.request.AuthenticationRequest;
import com.dxc.production.mimi.model.response.AuthenticationResponse;
import com.dxc.production.mimi.model.response.RegistrationResponse;
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
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
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
    public UserDto getUserInformation() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        UserEntity userEntity = userRepo.findByUsername(userDetails.getUsername());
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public AuthenticationResponse getJwt(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            // Since we are using username and password we then use UsernamePassword authentication token
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        // Verify the existence of the user from database
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String token = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse("Successfully retrieve token.", HttpStatus.OK, token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public RegistrationResponse registerUser(UserDto user) {
        StringBuilder errorMessage = new StringBuilder();
        boolean registrationResult = validateRegistrationUser(user, errorMessage);

        // If validation success, proceed to add
        if(registrationResult) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(user.getUsername().toLowerCase());
            newUser.setName(user.getName());
            newUser.setRole(user.getRole());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            try {
                userRepo.save(newUser);
                return new RegistrationResponse("Successfully created user.", HttpStatus.CREATED);
            } catch (Exception e) {
                return new RegistrationResponse("Error. Failed to create user.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new RegistrationResponse(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    public boolean validateRegistrationUser(UserDto user, StringBuilder errorMessage) {
        // Check username, name, role and password
        if (StringUtils.hasText(user.getUsername())
                && StringUtils.hasText(user.getName())
                && (user.getRole() == 0 || user.getRole() == 1)
                && StringUtils.hasText(user.getPassword())) {

            // Only allow digit, underscore and alphabets
            boolean validateUsername = Pattern.matches("[\\w]+", user.getUsername());

            // Only allow alphabets
            boolean validateName = Pattern.matches("[a-z]+", user.getName());

            // Should include 1 lower case & 1 upper case alphabet,  1 digit, and at least 1 of (@,#,$,%) -> Min length of 8, Max 16
            boolean validatePassword = Pattern.matches("^(?=(.*[0-9])+)(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[@#$%])+).{8,16}$", user.getPassword());
            if (!validateUsername) {
                errorMessage.append("Invalid Username: Only digit, underscore and alphabets allowed. ");
            }

            if (!validateName) {
                errorMessage.append("Invalid Name: Only alphabets allowed. ");
            }

            if (!validatePassword) {
                errorMessage.append("Invalid Password: Must include 1 lower case and 1 upper case alphabet, "
                                                                        + "1 digit and 1 special characters (@#$%). ");
            }
            return (validateUsername && validateName && validatePassword);
        } else {
            errorMessage.append("Error. Please ensure that all fields are filled up.");
        }
        return false;
    }
}
