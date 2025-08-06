package com.exhibyt.UserManagment.Utils;

import com.exhibyt.UserManagment.Entity.User;
import com.exhibyt.UserManagment.Exception.ResourceNotFoundException;
import com.exhibyt.UserManagment.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SecurityUtils {

    public static User getCurrentUser(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResourceNotFoundException("User not authenticated");
        }

        String username = authentication.getName();

        return userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UsernameNotFoundException("User not authenticated");
        }

        return authentication.getName();
    }
}
