package com.example.Ega.backend.controller;

import com.example.Ega.backend.dto.OperationRequest;
import com.example.Ega.backend.dto.ReleveRequest;
import com.example.Ega.backend.dto.TransactionDTO;
import com.example.Ega.backend.dto.VirementRequest;
import com.example.Ega.backend.service.CompteService;
import com.example.Ega.backend.service.TransactionService;
import com.example.Ega.backend.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private CompteService compteService;
    
    @Autowired
    private SecurityUtil securityUtil;
    
    @PostMapping("/depot")
    public ResponseEntity<TransactionDTO> depot(@Valid @RequestBody OperationRequest request) {
        // Vérifier que l'utilisateur peut effectuer un dépôt sur ce compte
        try {
            var compte = compteService.getCompteByNumero(request.getNumeroCompte());
            if (!securityUtil.isAdmin() && compte.getClientId() != null && !securityUtil.isClientOwner(compte.getClientId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(transactionService.depot(request));
    }
    
    @PostMapping("/retrait")
    public ResponseEntity<TransactionDTO> retrait(@Valid @RequestBody OperationRequest request) {
        // Vérifier que l'utilisateur peut effectuer un retrait sur ce compte
        try {
            var compte = compteService.getCompteByNumero(request.getNumeroCompte());
            if (!securityUtil.isAdmin() && compte.getClientId() != null && !securityUtil.isClientOwner(compte.getClientId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(transactionService.retrait(request));
    }
    
    @PostMapping("/virement")
    public ResponseEntity<TransactionDTO> virement(@Valid @RequestBody VirementRequest request) {
        // Vérifier que l'utilisateur peut effectuer un virement depuis ce compte
        try {
            var compteSource = compteService.getCompteByNumero(request.getCompteSource());
            if (!securityUtil.isAdmin() && compteSource.getClientId() != null && !securityUtil.isClientOwner(compteSource.getClientId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(transactionService.virement(request));
    }
    
    @GetMapping("/compte/{numeroCompte}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCompte(@PathVariable String numeroCompte) {
        // Vérifier que l'utilisateur peut voir les transactions de ce compte
        try {
            var compte = compteService.getCompteByNumero(numeroCompte);
            if (!securityUtil.isAdmin() && compte.getClientId() != null && !securityUtil.isClientOwner(compte.getClientId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(transactionService.getTransactionsByCompte(numeroCompte));
    }
    
    @PostMapping("/releve")
    public ResponseEntity<List<TransactionDTO>> getReleve(@Valid @RequestBody ReleveRequest request) {
        // Vérifier que l'utilisateur peut voir le relevé de ce compte
        try {
            var compte = compteService.getCompteByNumero(request.getNumeroCompte());
            if (!securityUtil.isAdmin() && compte.getClientId() != null && !securityUtil.isClientOwner(compte.getClientId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(transactionService.getTransactionsByCompteAndPeriod(
                request.getNumeroCompte(), request.getDateDebut(), request.getDateFin()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable String id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        // Vérifier que l'utilisateur peut voir cette transaction
        try {
            var compte = compteService.getCompteByNumero(transaction.getCompteNumero());
            if (!securityUtil.isAdmin() && compte.getClientId() != null && !securityUtil.isClientOwner(compte.getClientId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(transaction);
    }
}
