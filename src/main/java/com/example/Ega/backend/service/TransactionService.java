package com.example.Ega.backend.service;

import com.example.Ega.backend.dto.OperationRequest;
import com.example.Ega.backend.dto.TransactionDTO;
import com.example.Ega.backend.dto.VirementRequest;
import com.example.Ega.backend.entity.Compte;
import com.example.Ega.backend.entity.Transaction;
import com.example.Ega.backend.repository.CompteRepository;
import com.example.Ega.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CompteRepository compteRepository;
    
    @Autowired
    private CompteService compteService;
    
    public TransactionDTO depot(OperationRequest request) {
        Compte compte = compteService.findCompteEntityByNumero(request.getNumeroCompte());
        
        BigDecimal nouveauSolde = compte.getSolde().add(request.getMontant());
        
        Transaction transaction = new Transaction();
        transaction.setTypeTransaction(Transaction.TypeTransaction.DEPOT);
        transaction.setMontant(request.getMontant());
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setCompte(compte);
        transaction.setCompteId(compte.getId());
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Dépôt");
        transaction.setSoldeApres(nouveauSolde);
        
        compte.setSolde(nouveauSolde);
        compteRepository.save(compte);
        
        transaction = transactionRepository.save(transaction);
        return toDTO(transaction);
    }
    
    public TransactionDTO retrait(OperationRequest request) {
        Compte compte = compteService.findCompteEntityByNumero(request.getNumeroCompte());
        
        if (compte.getSolde().compareTo(request.getMontant()) < 0) {
            throw new RuntimeException("Solde insuffisant. Solde actuel: " + compte.getSolde());
        }
        
        BigDecimal nouveauSolde = compte.getSolde().subtract(request.getMontant());
        
        Transaction transaction = new Transaction();
        transaction.setTypeTransaction(Transaction.TypeTransaction.RETRAIT);
        transaction.setMontant(request.getMontant());
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setCompte(compte);
        transaction.setCompteId(compte.getId());
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Retrait");
        transaction.setSoldeApres(nouveauSolde);
        
        compte.setSolde(nouveauSolde);
        compteRepository.save(compte);
        
        transaction = transactionRepository.save(transaction);
        return toDTO(transaction);
    }
    
    public TransactionDTO virement(VirementRequest request) {
        Compte compteSource = compteService.findCompteEntityByNumero(request.getCompteSource());
        Compte compteDestinataire = compteService.findCompteEntityByNumero(request.getCompteDestinataire());
        
        if (compteSource.getId().equals(compteDestinataire.getId())) {
            throw new RuntimeException("Le compte source et le compte destinataire ne peuvent pas être identiques");
        }
        
        if (compteSource.getSolde().compareTo(request.getMontant()) < 0) {
            throw new RuntimeException("Solde insuffisant. Solde actuel: " + compteSource.getSolde());
        }
        
        BigDecimal nouveauSoldeSource = compteSource.getSolde().subtract(request.getMontant());
        BigDecimal nouveauSoldeDestinataire = compteDestinataire.getSolde().add(request.getMontant());
        
        LocalDateTime now = LocalDateTime.now();
        
        // Transaction sur le compte source
        Transaction transactionSource = new Transaction();
        transactionSource.setTypeTransaction(Transaction.TypeTransaction.VIREMENT);
        transactionSource.setMontant(request.getMontant());
        transactionSource.setDateTransaction(now);
        transactionSource.setCompte(compteSource);
        transactionSource.setCompteId(compteSource.getId());
        transactionSource.setCompteDestinataire(compteDestinataire);
        transactionSource.setDescription(request.getDescription() != null ? 
            "Virement vers " + compteDestinataire.getNumeroCompte() + " - " + request.getDescription() : 
            "Virement vers " + compteDestinataire.getNumeroCompte());
        transactionSource.setSoldeApres(nouveauSoldeSource);
        
        // Transaction sur le compte destinataire
        Transaction transactionDestinataire = new Transaction();
        transactionDestinataire.setTypeTransaction(Transaction.TypeTransaction.VIREMENT);
        transactionDestinataire.setMontant(request.getMontant());
        transactionDestinataire.setDateTransaction(now);
        transactionDestinataire.setCompte(compteDestinataire);
        transactionDestinataire.setCompteId(compteDestinataire.getId());
        transactionDestinataire.setCompteDestinataire(compteSource);
        transactionDestinataire.setDescription(request.getDescription() != null ? 
            "Virement reçu de " + compteSource.getNumeroCompte() + " - " + request.getDescription() : 
            "Virement reçu de " + compteSource.getNumeroCompte());
        transactionDestinataire.setSoldeApres(nouveauSoldeDestinataire);
        
        compteSource.setSolde(nouveauSoldeSource);
        compteDestinataire.setSolde(nouveauSoldeDestinataire);
        
        compteRepository.save(compteSource);
        compteRepository.save(compteDestinataire);
        
        transactionRepository.save(transactionSource);
        transactionRepository.save(transactionDestinataire);
        
        return toDTO(transactionSource);
    }
    
    public List<TransactionDTO> getTransactionsByCompte(String numeroCompte) {
        Compte compte = compteService.findCompteEntityByNumero(numeroCompte);
        return transactionRepository.findByCompte(compte).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getTransactionsByCompteAndPeriod(String numeroCompte, LocalDate dateDebut, LocalDate dateFin) {
        Compte compte = compteService.findCompteEntityByNumero(numeroCompte);
        LocalDateTime startDateTime = dateDebut.atStartOfDay();
        LocalDateTime endDateTime = dateFin.atTime(LocalTime.MAX);
        
        return transactionRepository.findByCompteIdAndDateTransactionBetween(
                compte.getId(), startDateTime, endDateTime)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public TransactionDTO getTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction non trouvée avec l'ID: " + id));
        return toDTO(transaction);
    }
    
    private TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setTypeTransaction(transaction.getTypeTransaction());
        dto.setMontant(transaction.getMontant());
        dto.setDateTransaction(transaction.getDateTransaction());
        dto.setCompteId(transaction.getCompte().getId());
        dto.setCompteNumero(transaction.getCompte().getNumeroCompte());
        if (transaction.getCompteDestinataire() != null) {
            dto.setCompteDestinataireId(transaction.getCompteDestinataire().getId());
            dto.setCompteDestinataireNumero(transaction.getCompteDestinataire().getNumeroCompte());
        }
        dto.setDescription(transaction.getDescription());
        dto.setSoldeApres(transaction.getSoldeApres());
        return dto;
    }
}
