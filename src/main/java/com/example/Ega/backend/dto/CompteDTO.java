package com.example.Ega.backend.dto;

import com.example.Ega.backend.entity.Compte;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteDTO {
    private String id;
    private String numeroCompte;
    
    @NotNull(message = "Le type de compte est obligatoire")
    private Compte.TypeCompte typeCompte;
    
    private LocalDate dateCreation;
    private BigDecimal solde;
    private String clientId;
    private String clientNom;
    private String clientPrenom;
}
