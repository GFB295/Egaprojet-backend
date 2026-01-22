package com.example.Ega.backend.service;

import com.example.Ega.backend.dto.CompteDTO;
import com.example.Ega.backend.entity.Client;
import com.example.Ega.backend.entity.Compte;
import com.example.Ega.backend.repository.ClientRepository;
import com.example.Ega.backend.repository.CompteRepository;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
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

    private final Random random = new Random();

    /**
     * Génère un numéro IBAN unique pour un nouveau compte
     * Format: FR + 2 chiffres de contrôle + 5 chiffres banque + 5 chiffres guichet + 11 chiffres compte + 2 chiffres clé
     */
    private String generateUniqueIban() {
        String iban;
        do {
            // Génération d'un numéro de compte aléatoire
            String bankCode = String.format("%05d", random.nextInt(100000)); // 5 chiffres banque
            String branchCode = String.format("%05d", random.nextInt(100000)); // 5 chiffres guichet  
            String accountNumber = String.format("%011d", random.nextLong() % 100000000000L); // 11 chiffres compte
            String nationalCheckDigits = String.format("%02d", random.nextInt(100)); // 2 chiffres clé
            
            // Construction de l'IBAN avec iban4j
            try {
                Iban ibanObj = new Iban.Builder()
                    .countryCode(CountryCode.FR)
                    .bankCode(bankCode)
                    .branchCode(branchCode)
                    .accountNumber(accountNumber)
                    .nationalCheckDigit(nationalCheckDigits)
                    .build();
                iban = ibanObj.toString();
            } catch (Exception e) {
                // En cas d'erreur, on génère un IBAN simple mais valide
                iban = Iban.random(CountryCode.FR).toString();
            }
        } while (compteRepository.existsByNumeroCompte(iban));
        
        return iban;
    }

    public List<CompteDTO> getComptesByClientId(String clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + clientId));
        
        return compteRepository.findByClient(client).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CompteDTO createCompte(CompteDTO compteDTO) {
        Client client = clientRepository.findById(compteDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Compte compte = new Compte();
        
        // Génération automatique d'un IBAN unique si non fourni
        String numeroCompte = compteDTO.getNumeroCompte();
        if (numeroCompte == null || numeroCompte.trim().isEmpty()) {
            numeroCompte = generateUniqueIban();
        } else {
            // Validation de l'IBAN fourni
            try {
                Iban.valueOf(numeroCompte);
            } catch (Exception e) {
                throw new RuntimeException("Le numéro IBAN fourni n'est pas valide: " + numeroCompte);
            }
            
            if (compteRepository.existsByNumeroCompte(numeroCompte)) {
                throw new RuntimeException("Le numéro de compte existe déjà");
            }
        }

        compte.setNumeroCompte(numeroCompte);
        compte.setTypeCompte(compteDTO.getTypeCompte());
        compte.setDateCreation(LocalDate.now());
        compte.setSolde(BigDecimal.ZERO); // Solde initial toujours à zéro selon les requirements
        compte.setClient(client);

        compte = compteRepository.save(compte);
        return toDTO(compte);
    }

    /**
     * Valide le format IBAN d'un numéro de compte
     */
    public boolean isValidIban(String iban) {
        if (iban == null || iban.trim().isEmpty()) {
            return false;
        }
        try {
            Iban.valueOf(iban.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
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

    public boolean isCompteOwner(String compteId, String username) {
        try {
            Compte compte = compteRepository.findById(compteId).orElse(null);
            return compte != null && compte.getClient() != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void deleteCompte(String id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec l'ID: " + id));
        
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
}
