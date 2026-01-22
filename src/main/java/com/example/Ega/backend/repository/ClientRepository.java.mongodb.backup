package com.example.Ega.backend.repository;

import com.example.Ega.backend.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByCourriel(String courriel);
    boolean existsByCourriel(String courriel);
}
