package com.example.Ega.backend.dto;

import com.example.Ega.backend.entity.Transaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String id;
    private Transaction.TypeTransaction typeTransaction;
    
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal montant;
    
    private LocalDateTime dateTransaction;
    private String compteId;
    private String compteNumero;
    private String compteDestinataireId;
    private String compteDestinataireNumero;
    private String description;
    private BigDecimal soldeApres;
}
