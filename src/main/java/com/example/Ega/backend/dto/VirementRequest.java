package com.example.Ega.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirementRequest {
    @NotNull(message = "Le numéro de compte source est obligatoire")
    private String compteSource;
    
    @NotNull(message = "Le numéro de compte destinataire est obligatoire")
    private String compteDestinataire;
    
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal montant;
    
    private String description;
}
