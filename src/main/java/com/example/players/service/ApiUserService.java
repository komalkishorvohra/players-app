package com.example.players.service;

import com.example.players.entity.ApiUser;
import com.example.players.repository.ApiUserRepository;
import com.example.players.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApiUserService {
    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String login(String username, String password) {
        ApiUser apiUser = apiUserRepository.findByUsername(username);
        if (apiUser != null && passwordEncoder.matches(password, apiUser.getPassword())) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid Credentials");
        }
    }

    public void register(String username, String password) {
        ApiUser apiUser = new ApiUser();
        apiUser.setUsername(username);
        apiUser.setPassword(passwordEncoder.encode(password));
        apiUserRepository.save(apiUser);
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApiUser apiUser = apiUserRepository.findByUsername(username);
        if (apiUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return User.builder()
                .username(apiUser.getUsername())
                .password(apiUser.getPassword())
                .authorities("ROLE_USER") // You can assign roles or authorities here
                .build();
    }
}
