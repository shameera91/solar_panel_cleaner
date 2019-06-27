package com.app.board.controller;

import com.app.board.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Util {
    static String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserPrincipal) principal).getEmail();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
