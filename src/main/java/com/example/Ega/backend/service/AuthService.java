package com.example.Ega.backend.service;

import com.example.Ega.backend.dto.AuthRequest;
import com.example.Ega.backend.dto.AuthResponse;
import com.example.Ega.backend.dto.RegisterRequest;
import com.example.Ega.backend.entity.Client;
import com.example.Ega.backend.entity.User;
import com.example.Ega.backend.repository.ClientRepository;
import com.example.Ega.backend.repository.UserRepository;
import com.example.Ega.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Le nom d'utilisateur existe déjà");
        }
        
        if (clientRepository.existsByCourriel(request.getCourriel())) {
            throw new RuntimeException("Le courriel existe déjà");
        }
        
        Client client = new Client();
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setDateNaissance(request.getDateNaissance());
        client.setSexe(request.getSexe());
        client.setAdresse(request.getAdresse());
        client.setTelephone(request.getTelephone());
        client.setCourriel(request.getCourriel());
        client.setNationalite(request.getNationalite());
        
        client = clientRepository.save(client);
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setClient(client);
        user.setRole("ROLE_CLIENT"); // Définir explicitement le rôle client
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        user = userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getUsername());
        
        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), client.getId(), user.getRole());
    }
    
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user.getUsername());
        
        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), user.getClient() != null ? user.getClient().getId() : null, user.getRole());
    }

    @Transactional
    public void initAdmin(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("L'utilisateur admin existe déjà");
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole("ROLE_ADMIN");
        admin.setClient(null); // Admin n'a pas de client associé
        admin.setEnabled(true);
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);

        userRepository.save(admin);
    }
}
