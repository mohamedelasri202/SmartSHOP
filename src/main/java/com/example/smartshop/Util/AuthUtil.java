package com.example.smartshop.Util;

import com.example.smartshop.Models.Enums.UserRole;
import com.example.smartshop.Models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public Boolean isAdmin(HttpSession session) {
        if(session == null ){
            return false;
        }
        UserRole role =(UserRole)  session.getAttribute("userRole");

        return role == UserRole.ADMIN ;

    }
    public Long getLoggedInUserId(HttpSession session) {
        if (session == null) {

            return null;
        }
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return null;
        }
        return userId;
    }

    public Boolean isClient(HttpSession session) {
        if (session == null) {
            return false;
        }
        UserRole role = (UserRole) session.getAttribute("userRole");
        return role != null && role == UserRole.CLIENT;
    }
}
