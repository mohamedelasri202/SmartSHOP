package com.example.smartshop.Services;

import com.example.smartshop.Models.User;
import com.example.smartshop.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.PasswordAuthentication;

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void login(String username, String password){
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Username not found"));

    }
}
