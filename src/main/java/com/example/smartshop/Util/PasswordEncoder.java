package com.example.smartshop.Util;

public interface PasswordEncoder {
    String encode(CharSequence rawPassword);
    Boolean checkPassword(String rawPassword, String encodedPassword);
}
