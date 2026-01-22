package com.example.Ega.backend.controller;

import com.example.Ega.backend.dto.CompteDTO;
import com.example.Ega.backend.service.CompteService;
import com.example.Ega.backend.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
@CrossOrigin(origins = "http://localhost:4200")
public class CompteController {

    @Autowired
    private CompteService compteService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<CompteDTO> createCompte(@Valid @RequestBody CompteDTO compteDTO) {
        String currentClientId = SecurityUtil.getCurrentClientId();
        if (currentClientId == null) {
            return ResponseEntity.badRequest().build();
        }
        compteDTO.setClientId(currentClientId);
        return ResponseEntity.ok(compteService.createCompte(compteDTO));
    }

    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<CompteDTO>> getComptesByCurrentClient() {
        String currentClientId = SecurityUtil.getCurrentClientId();
        if (currentClientId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(compteService.getComptesByClientId(currentClientId));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CompteDTO>> getComptesByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(compteService.getComptesByClientId(clientId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENT') and @compteService.isCompteOwner(#id, authentication.name))")
    public ResponseEntity<CompteDTO> getCompteById(@PathVariable String id) {
        return ResponseEntity.ok(compteService.getCompteById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CompteDTO>> getAllComptes() {
        return ResponseEntity.ok(compteService.getAllComptes());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENT') and @compteService.isCompteOwner(#id, authentication.name))")
    public ResponseEntity<Void> deleteCompte(@PathVariable String id) {
        String currentClientId = SecurityUtil.getCurrentClientId();
        if (currentClientId == null) {
            return ResponseEntity.badRequest().build();
        }
        compteService.deleteCompte(id);
        return ResponseEntity.noContent().build();
    }
}
