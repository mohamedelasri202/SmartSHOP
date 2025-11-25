package com.example.smartshop.Controllers;


import com.example.smartshop.DTO.LoginRequest;
import com.example.smartshop.Models.User;
import com.example.smartshop.Repositories.UserRepository;
import com.example.smartshop.Services.AuthServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthServiceInterface authService;


    public AuthController(AuthServiceInterface authService) {
        this.authService = authService;

    }
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request ,HttpSession  session) {

        User user = authService.login(request.getUserName(),request.getPassword());

        session.setAttribute("userID",user.getId());
        session.setAttribute("userRole",user.getRole());

        return ResponseEntity.ok("login success");

    }


}
