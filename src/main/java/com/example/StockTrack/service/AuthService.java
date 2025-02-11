package com.example.StockTrack.service;

import com.example.StockTrack.dtos.AuthRequest;
import com.example.StockTrack.dtos.AuthResponse;
import com.example.StockTrack.entity.User;
import com.example.StockTrack.enums.Role;
import com.example.StockTrack.exceptions.IncorrectCredentialsException;
import com.example.StockTrack.repository.UserRepository;
import com.example.StockTrack.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.member;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(AuthRequest authRequest){
        Optional <User> userOptional = this.userRepository.findByEmail(authRequest.getEmail());
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException(String.format("Member not found  %s ",authRequest.getEmail()));
        }
        User user = userOptional.get();
        if(!this.passwordEncoder.matches(authRequest.getPassword(), user.getPassword())){
            throw new IncorrectCredentialsException("Incorrect password");
        }
        return user;


    }

    public User addMember(User user){

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        User savedMember = this.userRepository.save(member);

        return savedMember;
    }
}
