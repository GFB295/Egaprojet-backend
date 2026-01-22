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
import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    private String id;
    
    @NotNull(message = "Le type de transaction est obligatoire")
    private TypeTransaction typeTransaction;
    
    @NotNull(message = "Le montant est obligatoire")
    private BigDecimal montant;
    
    @NotNull(message = "La date est obligatoire")
    @Indexed
    private LocalDateTime dateTransaction;
    
    @NotNull(message = "Le compte est obligatoire")
    @DBRef(lazy = true)
    private Compte compte;
    
    private String compteId; // Stocke aussi l'ID pour faciliter les requêtes
    
    @DBRef(lazy = true)
    private Compte compteDestinataire;
    
    private String description;
    
    @NotNull(message = "Le solde après transaction est obligatoire")
    private BigDecimal soldeApres;
    
    public enum TypeTransaction {
        DEPOT,
        RETRAIT,
        VIREMENT
    }
}
