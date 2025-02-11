package com.example.StockTrack.service;

import com.example.StockTrack.entity.User;
import com.example.StockTrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByEmail(username);

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User not found: %s", username));
        }

        User user = userOpt.get();

        // Convert role into GrantedAuthority
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRoles()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
