package com.example.Ega.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleveRequest {
    @NotNull(message = "Le numéro de compte est obligatoire")
    private String numeroCompte;
    
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;
    
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;
}
