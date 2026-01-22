package com.example.Ega.backend.util;

import com.example.Ega.backend.entity.User;
import com.example.Ega.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    
    private static UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        SecurityUtil.userRepository = userRepository;
    }
    
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElse(null);
    }
    
    public static String getCurrentClientId() {
        User user = getCurrentUser();
        if (user != null && user.getClient() != null) {
            return user.getClient().getId().toString();
        }
        return null;
    }
    
    public static boolean isAdmin() {
        User user = getCurrentUser();
        return user != null && "ROLE_ADMIN".equals(user.getRole());
    }
    
    public static boolean isClientOwner(String clientId) {
        if (isAdmin()) {
            return true; // Admin peut tout voir
        }
        String currentClientId = getCurrentClientId();
        return currentClientId != null && currentClientId.equals(clientId);
    }
}


