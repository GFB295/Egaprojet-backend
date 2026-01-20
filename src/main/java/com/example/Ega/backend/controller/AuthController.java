package com.example.Ega.backend.controller;

import com.example.Ega.backend.dto.AuthRequest;
import com.example.Ega.backend.dto.AuthResponse;
import com.example.Ega.backend.dto.RegisterRequest;
import com.example.Ega.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/init-admin")
    public ResponseEntity<String> initAdmin(@RequestParam(required = false) String username, 
                                             @RequestParam(required = false) String password) {
        try {
            // Valeurs par défaut si non fournies
            String adminUsername = username != null ? username : "admin";
            String adminPassword = password != null ? password : "Admin@123";
            
            authService.initAdmin(adminUsername, adminPassword);
            return ResponseEntity.ok("Admin créé avec succès ! Username: " + adminUsername + ", Password: " + adminPassword);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
