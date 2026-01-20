package com.example.Ega.backend.repository;

import com.example.Ega.backend.entity.Compte;
import com.example.Ega.backend.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteRepository extends MongoRepository<Compte, String> {
    Optional<Compte> findByNumeroCompte(String numeroCompte);
    boolean existsByNumeroCompte(String numeroCompte);
    List<Compte> findByClient(Client client);
    List<Compte> findByClientId(String clientId);
}
