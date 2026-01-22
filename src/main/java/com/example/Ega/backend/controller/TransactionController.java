package com.example.Ega.backend.controller;

import com.example.Ega.backend.dto.OperationRequest;
import com.example.Ega.backend.dto.TransactionDTO;
import com.example.Ega.backend.dto.VirementRequest;
import com.example.Ega.backend.service.TransactionService;
import com.example.Ega.backend.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/depot")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<TransactionDTO> effectuerDepot(@Valid @RequestBody OperationRequest request) {
        String currentClientId = SecurityUtil.getCurrentClientId();
        return ResponseEntity.ok(transactionService.effectuerDepot(request));
    }

    @PostMapping("/retrait")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<TransactionDTO> effectuerRetrait(@Valid @RequestBody OperationRequest request) {
        String currentClientId = SecurityUtil.getCurrentClientId();
        return ResponseEntity.ok(transactionService.effectuerRetrait(request));
    }

    @PostMapping("/virement")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<List<TransactionDTO>> effectuerVirement(@Valid @RequestBody VirementRequest request) {
        String currentClientId = SecurityUtil.getCurrentClientId();
        return ResponseEntity.ok(transactionService.effectuerVirement(request));
    }

    @GetMapping("/compte/{compteId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENT') and @compteService.isCompteOwner(#compteId, authentication.name))")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCompte(@PathVariable String compteId) {
        String currentClientId = SecurityUtil.getCurrentClientId();
        return ResponseEntity.ok(transactionService.getTransactionsByCompteId(compteId));
    }

    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCurrentClient() {
        String currentClientId = SecurityUtil.getCurrentClientId();
        return ResponseEntity.ok(transactionService.getTransactionsByClientId(currentClientId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}
