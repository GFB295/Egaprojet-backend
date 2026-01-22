package com.example.Ega.backend.controller;

import com.example.Ega.backend.entity.User;
import com.example.Ega.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Backend MongoDB is running");
        response.put("timestamp", LocalDateTime.now());
        response.put("database", "MongoDB");
        return response;
    }
    
    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "EGA Bank");
        response.put("version", "1.0.0");
        response.put("database", "MongoDB");
        response.put("port", 8080);
        return response;
    }
    
    @PostMapping("/promote-admin/{username}")
    public Map<String, Object> promoteToAdmin(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + username));
            
            user.setRole("ROLE_ADMIN");
            userRepository.save(user);
            
            response.put("status", "SUCCESS");
            response.put("message", "Utilisateur " + username + " promu admin avec succès");
            response.put("username", username);
            response.put("newRole", "ROLE_ADMIN");
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/user/{username}")
    public Map<String, Object> getUserInfo(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + username));
            
            response.put("status", "SUCCESS");
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("enabled", user.isEnabled());
            response.put("accountNonExpired", user.isAccountNonExpired());
            response.put("accountNonLocked", user.isAccountNonLocked());
            response.put("credentialsNonExpired", user.isCredentialsNonExpired());
            response.put("hasClient", user.getClient() != null);
            response.put("passwordLength", user.getPassword() != null ? user.getPassword().length() : 0);
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
        }
        
        return response;
    }
}