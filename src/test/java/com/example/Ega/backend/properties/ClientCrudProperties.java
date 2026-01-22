package com.example.Ega.backend.properties;

import com.example.Ega.backend.config.PropertyTestConfig;
import com.example.Ega.backend.entity.Client;
import com.example.Ega.backend.generators.ClientGenerators;
import com.example.Ega.backend.repository.ClientRepository;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests basés sur les propriétés pour les opérations CRUD des clients
 * **Property 1: Client CRUD Operations**
 * **Validates: Requirements 1.1**
 */
@Tag("Feature: ega-bank, Property 1: Client CRUD Operations")
public class ClientCrudProperties extends PropertyTestConfig {

    @Autowired
    private ClientRepository clientRepository;

    @Property(tries = 50)
    void clientCanBeCreatedAndRetrieved(@ForAll("validClients") Client client) {
        // Given: Un client valide
        
        // When: Le client est sauvegardé
        Client savedClient = clientRepository.save(client);
        
        // Then: Le client peut être récupéré avec le même ID
        assertThat(savedClient.getId()).isNotNull();
        
        Client retrievedClient = clientRepository.findById(savedClient.getId()).orElse(null);
        assertThat(retrievedClient).isNotNull();
        assertThat(retrievedClient.getNom()).isEqualTo(client.getNom());
        assertThat(retrievedClient.getPrenom()).isEqualTo(client.getPrenom());
        assertThat(retrievedClient.getCourriel()).isEqualTo(client.getCourriel());
        
        // Cleanup
        clientRepository.deleteById(savedClient.getId());
    }

    @Property(tries = 30)
    void clientCanBeUpdated(@ForAll("validClients") Client client) {
        // Given: Un client sauvegardé
        Client savedClient = clientRepository.save(client);
        
        // When: Le client est modifié
        String newNom = "NouveauNom";
        savedClient.setNom(newNom);
        Client updatedClient = clientRepository.save(savedClient);
        
        // Then: Les modifications sont persistées
        assertThat(updatedClient.getNom()).isEqualTo(newNom);
        
        Client retrievedClient = clientRepository.findById(savedClient.getId()).orElse(null);
        assertThat(retrievedClient).isNotNull();
        assertThat(retrievedClient.getNom()).isEqualTo(newNom);
        
        // Cleanup
        clientRepository.deleteById(savedClient.getId());
    }

    @Property(tries = 30)
    void clientCanBeDeleted(@ForAll("validClients") Client client) {
        // Given: Un client sauvegardé
        Client savedClient = clientRepository.save(client);
        String clientId = savedClient.getId();
        
        // When: Le client est supprimé
        clientRepository.deleteById(clientId);
        
        // Then: Le client n'existe plus
        boolean exists = clientRepository.existsById(clientId);
        assertThat(exists).isFalse();
    }

    // Générateur personnalisé pour les clients valides
    static Arbitrary<Client> validClients() {
        return ClientGenerators.validClients();
    }
}