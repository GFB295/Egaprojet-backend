package com.example.Ega.backend.repository;

import com.example.Ega.backend.entity.Transaction;
import com.example.Ega.backend.entity.Compte;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByCompte(Compte compte);
    List<Transaction> findByCompteId(String compteId);
    
    @Query("{ 'compteId': ?0, 'dateTransaction': { $gte: ?1, $lte: ?2 } }")
    List<Transaction> findByCompteIdAndDateTransactionBetween(
        String compteId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
}
