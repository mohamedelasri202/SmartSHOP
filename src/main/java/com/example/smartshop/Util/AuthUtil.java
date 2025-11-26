package com.example.smartshop.Util;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public Boolean isAdmin(HttpSession session) {
        if(session == null ){
            return false;
        }
        String role = (String)session.getAttribute("UserRole");
        if(role == null){
            return false;
        }
        return role.equals("Admin");

    }
}
