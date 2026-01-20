package com.example.Ega.backend.service;

import com.example.Ega.backend.dto.CompteDTO;
import com.example.Ega.backend.entity.Client;
import com.example.Ega.backend.entity.Compte;
import com.example.Ega.backend.repository.ClientRepository;
import com.example.Ega.backend.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompteService {
    
    @Autowired
    private CompteRepository compteRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    public CompteDTO createCompte(String clientId, Compte.TypeCompte typeCompte) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + clientId));
        
        // Generate unique IBAN
        String numeroCompte = null;
        int attempts = 0;
        Random random = new Random();
        
        while (numeroCompte == null) {
            attempts++;
            if (attempts > 10) {
                throw new RuntimeException("Impossible de générer un numéro de compte unique");
            }
            
            // Generate a random French IBAN
            // Format IBAN français: FR + 2 chiffres de contrôle + 23 caractères (code banque + code guichet + numéro compte + clé RIB)
            long baseNumber = Math.abs(clientId.hashCode() % 100000000000L) + 
                             (typeCompte == Compte.TypeCompte.COURANT ? 10000000000L : 20000000000L) +
                             random.nextInt(1000);
            
            // Générer un IBAN français valide
            String bankCode = String.format("%05d", 12345 + random.nextInt(1000));
            String branchCode = String.format("%05d", 67890 + random.nextInt(1000));
            String accountNumber = String.format("%011d", baseNumber);
            String ribKey = String.format("%02d", random.nextInt(100));
            
            // Construire le BBAN (Basic Bank Account Number)
            String bban = bankCode + branchCode + accountNumber + ribKey;
            
            // Calculer les chiffres de contrôle IBAN (mod 97)
            // Réorganiser: BBAN + "FR" + "00"
            String rearranged = bban + "FR00";
            StringBuilder numericString = new StringBuilder();
            for (char c : rearranged.toCharArray()) {
                if (Character.isDigit(c)) {
                    numericString.append(c);
                } else {
                    numericString.append((c - 'A' + 10));
                }
            }
            
            // Calcul mod 97
            java.math.BigInteger bigInt = new java.math.BigInteger(numericString.toString());
            long remainder = bigInt.mod(java.math.BigInteger.valueOf(97)).longValue();
            long checkDigits = 98 - remainder;
            
            String generatedIban = String.format("FR%02d%s", checkDigits, bban);
            
            // Valider l'IBAN - vérifier si le numéro est unique
            if (!compteRepository.existsByNumeroCompte(generatedIban)) {
                numeroCompte = generatedIban;
            }
        }
        
        Compte compte = new Compte();
        compte.setNumeroCompte(numeroCompte);
        compte.setTypeCompte(typeCompte);
        compte.setDateCreation(LocalDate.now());
        compte.setSolde(BigDecimal.ZERO);
        compte.setClient(client);
        
        compte = compteRepository.save(compte);
        return toDTO(compte);
    }
    
    public CompteDTO getCompteById(String id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec l'ID: " + id));
        return toDTO(compte);
    }
    
    public CompteDTO getCompteByNumero(String numeroCompte) {
        Compte compte = compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec le numéro: " + numeroCompte));
        return toDTO(compte);
    }
    
    public List<CompteDTO> getAllComptes() {
        return compteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<CompteDTO> getComptesByClientId(String clientId) {
        return compteRepository.findByClientId(clientId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public void deleteCompte(String id) {
        if (!compteRepository.existsById(id)) {
            throw new RuntimeException("Compte non trouvé avec l'ID: " + id);
        }
        compteRepository.deleteById(id);
    }
    
    private CompteDTO toDTO(Compte compte) {
        CompteDTO dto = new CompteDTO();
        dto.setId(compte.getId());
        dto.setNumeroCompte(compte.getNumeroCompte());
        dto.setTypeCompte(compte.getTypeCompte());
        dto.setDateCreation(compte.getDateCreation());
        dto.setSolde(compte.getSolde());
        dto.setClientId(compte.getClient().getId());
        dto.setClientNom(compte.getClient().getNom());
        dto.setClientPrenom(compte.getClient().getPrenom());
        return dto;
    }
    
    public Compte findCompteEntityByNumero(String numeroCompte) {
        return compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec le numéro: " + numeroCompte));
    }
}
