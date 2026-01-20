package com.example.Ega.backend.controller;

import com.example.Ega.backend.dto.CompteDTO;
import com.example.Ega.backend.entity.Compte;
import com.example.Ega.backend.service.CompteService;
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
    
    @PostMapping("/client/{clientId}")
    public ResponseEntity<CompteDTO> createCompte(
            @PathVariable String clientId,
            @RequestParam Compte.TypeCompte typeCompte) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compteService.createCompte(clientId, typeCompte));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CompteDTO> getCompteById(@PathVariable String id) {
        return ResponseEntity.ok(compteService.getCompteById(id));
    }
    
    @GetMapping("/numero/{numeroCompte}")
    public ResponseEntity<CompteDTO> getCompteByNumero(@PathVariable String numeroCompte) {
        return ResponseEntity.ok(compteService.getCompteByNumero(numeroCompte));
    }
    
    @GetMapping
    public ResponseEntity<List<CompteDTO>> getAllComptes() {
        return ResponseEntity.ok(compteService.getAllComptes());
    }
    
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CompteDTO>> getComptesByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(compteService.getComptesByClientId(clientId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompte(@PathVariable String id) {
        compteService.deleteCompte(id);
        return ResponseEntity.noContent().build();
    }
}
