package com.example.StockTrack.controller;

import com.example.StockTrack.dtos.AuthRequest;
import com.example.StockTrack.dtos.AuthResponse;
import com.example.StockTrack.entity.User;
import com.example.StockTrack.service.AuthService;
import com.example.StockTrack.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody AuthRequest authDto) {
        try {
            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = this.jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(userDetails, jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> addMember(@RequestBody User user) {
        User memberToAdd = this.authService.addMember(user);
        UserDetails userDetails = new User(memberToAdd.getEmail(), memberToAdd.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRoles())));

        String jwtToken = this.jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(userDetails, jwtToken));
    }



}
