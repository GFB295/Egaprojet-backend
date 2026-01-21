package com.example.Ega.backend.controller;

import com.example.Ega.backend.dto.CompteDTO;
import com.example.Ega.backend.entity.Compte;
import com.example.Ega.backend.service.CompteService;
import com.example.Ega.backend.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
@CrossOrigin(origins = "http://localhost:4200")
public class CompteController {
    
    @Autowired
    private CompteService compteService;
    
    @Autowired
    private SecurityUtil securityUtil;
    
    @PostMapping("/client/{clientId}")
    public ResponseEntity<CompteDTO> createCompte(
            @PathVariable String clientId,
            @RequestParam Compte.TypeCompte typeCompte) {
        // Vérifier que l'utilisateur peut créer un compte pour ce client
        if (!securityUtil.isAdmin() && !securityUtil.isClientOwner(clientId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(compteService.createCompte(clientId, typeCompte));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CompteDTO> getCompteById(@PathVariable String id) {
        CompteDTO compte = compteService.getCompteById(id);
        // Vérifier que l'utilisateur peut voir ce compte
        if (!securityUtil.isAdmin() && compte.getClientId() != null && !securityUtil.isClientOwner(compte.getClientId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(compte);
    }
    
    @GetMapping("/numero/{numeroCompte}")
    public ResponseEntity<CompteDTO> getCompteByNumero(@PathVariable String numeroCompte) {
        CompteDTO compte = compteService.getCompteByNumero(numeroCompte);
        // Vérifier que l'utilisateur peut voir ce compte
        if (!securityUtil.isAdmin() && compte.getClientId() != null && !securityUtil.isClientOwner(compte.getClientId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(compte);
    }
    
    @GetMapping
    public ResponseEntity<List<CompteDTO>> getAllComptes() {
        // Seuls les admins peuvent voir tous les comptes
        if (!securityUtil.isAdmin()) {
            String clientId = securityUtil.getCurrentClientId();
            if (clientId == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(compteService.getComptesByClientId(clientId));
        }
        return ResponseEntity.ok(compteService.getAllComptes());
    }
    
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CompteDTO>> getComptesByClientId(@PathVariable String clientId) {
        // Vérifier que l'utilisateur peut voir les comptes de ce client
        if (!securityUtil.isAdmin() && !securityUtil.isClientOwner(clientId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(compteService.getComptesByClientId(clientId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompte(@PathVariable String id) {
        CompteDTO compte = compteService.getCompteById(id);
        // Vérifier que l'utilisateur peut supprimer ce compte
        if (!securityUtil.isAdmin() && compte.getClientId() != null && !securityUtil.isClientOwner(compte.getClientId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        compteService.deleteCompte(id);
        return ResponseEntity.noContent().build();
    }
}
