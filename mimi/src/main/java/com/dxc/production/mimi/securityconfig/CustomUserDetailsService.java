package com.dxc.production.mimi.securityconfig;

import com.dxc.production.mimi.dao.UserRepo;
import com.dxc.production.mimi.enumerate.Role;
import com.dxc.production.mimi.enumerate.Status;
import com.dxc.production.mimi.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // User object are used to compare with incoming authentication object credentials
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles = null;

        UserEntity user = userRepo.findByUsername(username);
        if (user != null && user.getDeleteFlag() == Status.AVAILABLE.getValue()) {
            roles = Arrays.asList(new SimpleGrantedAuthority(Role.valueToRole(user.getRole()).name())); // Assign Roles based on role integer
            return new User(user.getUsername(), user.getPassword(), roles);
        }
        throw new UsernameNotFoundException("User not found with the username " + username);
    }
}
