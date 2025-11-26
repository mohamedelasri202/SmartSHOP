package com.example.smartshop.Util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptCustomEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword){
        return BCrypt.hashpw(rawPassword.toString(),BCrypt.gensalt());
    }
    @Override
    public Boolean checkPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
