package com.example.smartshop.Services;

import com.example.smartshop.Exceptions.AuthenticationFailedException;
import com.example.smartshop.Models.User;
import com.example.smartshop.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User login(String username, String password){
        User user = userRepository.findByUsername(username).orElseThrow(()->new AuthenticationFailedException("Invalid username or password"));
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new AuthenticationFailedException("Invalid username or password");
        }
        return user;

    }
}
