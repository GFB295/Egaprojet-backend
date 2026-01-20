package com.example.Ega.backend.controller;

import com.example.Ega.backend.dto.ReleveRequest;
import com.example.Ega.backend.dto.TransactionDTO;
import com.example.Ega.backend.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/releves")
@CrossOrigin(origins = "http://localhost:4200")
public class ReleveController {
    
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping("/imprimer")
    public ResponseEntity<byte[]> imprimerReleve(@Valid @RequestBody ReleveRequest request) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByCompteAndPeriod(
                request.getNumeroCompte(), request.getDateDebut(), request.getDateFin());
        
        String releveContent = generateReleveContent(request.getNumeroCompte(), 
                request.getDateDebut(), request.getDateFin(), transactions);
        
        byte[] pdfBytes = releveContent.getBytes(StandardCharsets.UTF_8);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", 
                "releve_" + request.getNumeroCompte() + ".txt");
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
    
    private String generateReleveContent(String numeroCompte, 
                                        java.time.LocalDate dateDebut, 
                                        java.time.LocalDate dateFin,
                                        List<TransactionDTO> transactions) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        sb.append("========================================\n");
        sb.append("      RELEVE BANCAIRE\n");
        sb.append("========================================\n\n");
        sb.append("Numéro de compte: ").append(numeroCompte).append("\n");
        sb.append("Période: ").append(dateDebut.format(dateFormatter))
          .append(" au ").append(dateFin.format(dateFormatter)).append("\n\n");
        sb.append("========================================\n");
        sb.append(String.format("%-20s %-15s %-15s %-20s\n", 
                "Date", "Type", "Montant", "Solde après"));
        sb.append("========================================\n");
        
        for (TransactionDTO transaction : transactions) {
            sb.append(String.format("%-20s %-15s %-15s %-20s\n",
                    transaction.getDateTransaction().format(formatter),
                    transaction.getTypeTransaction().name(),
                    transaction.getMontant().toString(),
                    transaction.getSoldeApres().toString()));
            if (transaction.getDescription() != null) {
                sb.append("  Description: ").append(transaction.getDescription()).append("\n");
            }
            sb.append("\n");
        }
        
        sb.append("========================================\n");
        sb.append("Nombre total de transactions: ").append(transactions.size()).append("\n");
        sb.append("========================================\n");
        
        return sb.toString();
    }
}
