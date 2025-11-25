package com.example.smartshop.Services;

import com.example.smartshop.Models.User;

public interface AuthServiceInterface {

    public User login(String username, String password);
}
