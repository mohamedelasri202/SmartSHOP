package com.example.smartshop.Config;

import com.example.smartshop.Models.Enums.UserRole;
import com.example.smartshop.Models.User;
import com.example.smartshop.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration

public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void run (String ... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {

            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setRole(UserRole.ADMIN);
            userRepository.save(user);
            System.out.println("Admin has been created");


        }
    }

}
