package com.dxc.production.mimi.securityconfig;

import com.dxc.production.mimi.dao.UserRepo;
import com.dxc.production.mimi.service.UserServiceInterface;
import io.jsonwebtoken.*;
import com.dxc.production.mimi.enumerate.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtil {
    private String secret = "Fv$?o$f!@Oj*1bN#b)8;/Av=ep5Z~8" ;
    private int jwtExpirationInMs = 18000000;   // 5 hours
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    // generate token for user, add additional claims
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        // Add the roles based on their roles
        if (roles.contains(new SimpleGrantedAuthority(Role.ADMIN.name()))) {
            claims.put("isAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority(Role.USER.name()))) {
            claims.put("isAdmin", false);
        }

        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Generate token based on subject and other claims such as "isAdmin"
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)).signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public boolean validateToken(String authToken) {
        try {
            // Verify Jwt token has not been tampered with
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(ex.getHeader(), ex.getClaims(), "Token has Expired", ex);
        }
    }

    // Get username from token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();    // Sign to get subject which is user
        return claims.getSubject();
    }

    // Check claims from token, check the roles
    public List<SimpleGrantedAuthority> getRolesFromToken(String authToken) {
        List<SimpleGrantedAuthority> roles = null;
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
        Boolean isAdmin = claims.get("isAdmin", Boolean.class); // Token consist of 1 key pair: 'isAdmin' : T/F

        if (isAdmin) {
            roles = Arrays.asList(new SimpleGrantedAuthority(Role.ADMIN.name())); // Add "ADMIN"
        }
        else {
            roles = Arrays.asList(new SimpleGrantedAuthority(Role.USER.name()));    // Add "USER"
        }
        return roles;
    }
}
