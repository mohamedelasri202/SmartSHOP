package com.example.smartshop.DTO;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}
