package com.example.Ega.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "comptes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compte {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String numeroCompte;
    
    @NotNull(message = "Le type de compte est obligatoire")
    private TypeCompte typeCompte;
    
    @NotNull(message = "La date de création est obligatoire")
    private LocalDate dateCreation;
    
    @NotNull(message = "Le solde est obligatoire")
    private BigDecimal solde;
    
    @NotNull(message = "Le client est obligatoire")
    @DBRef(lazy = true)
    private Client client;
    
    @DBRef(lazy = true)
    private List<Transaction> transactions;
    
    public enum TypeCompte {
        COURANT,
        EPARGNE
    }
}
