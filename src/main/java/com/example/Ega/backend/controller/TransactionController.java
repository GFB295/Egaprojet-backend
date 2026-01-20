package com.example.Ega.backend.controller;

import com.example.Ega.backend.dto.OperationRequest;
import com.example.Ega.backend.dto.ReleveRequest;
import com.example.Ega.backend.dto.TransactionDTO;
import com.example.Ega.backend.dto.VirementRequest;
import com.example.Ega.backend.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping("/depot")
    public ResponseEntity<TransactionDTO> depot(@Valid @RequestBody OperationRequest request) {
        return ResponseEntity.ok(transactionService.depot(request));
    }
    
    @PostMapping("/retrait")
    public ResponseEntity<TransactionDTO> retrait(@Valid @RequestBody OperationRequest request) {
        return ResponseEntity.ok(transactionService.retrait(request));
    }
    
    @PostMapping("/virement")
    public ResponseEntity<TransactionDTO> virement(@Valid @RequestBody VirementRequest request) {
        return ResponseEntity.ok(transactionService.virement(request));
    }
    
    @GetMapping("/compte/{numeroCompte}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCompte(@PathVariable String numeroCompte) {
        return ResponseEntity.ok(transactionService.getTransactionsByCompte(numeroCompte));
    }
    
    @PostMapping("/releve")
    public ResponseEntity<List<TransactionDTO>> getReleve(@Valid @RequestBody ReleveRequest request) {
        return ResponseEntity.ok(transactionService.getTransactionsByCompteAndPeriod(
                request.getNumeroCompte(), request.getDateDebut(), request.getDateFin()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable String id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
}
