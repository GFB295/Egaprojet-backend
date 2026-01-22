package com.example.Ega.backend.generators;

import com.example.Ega.backend.entity.Client;
import com.example.Ega.backend.entity.Compte;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.time.api.Dates;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Générateurs de données pour les tests basés sur les propriétés des entités Compte
 */
public class CompteGenerators {

    /**
     * Génère des comptes valides avec des données réalistes
     */
    public static Arbitrary<Compte> validComptes() {
        return Combinators.combine(
            validIbans(),
            typesCompte(),
            datesCreation(),
            soldesValides(),
            ClientGenerators.validClients()
        ).as((iban, type, date, solde, client) -> {
            Compte compte = new Compte();
            compte.setNumeroCompte(iban);
            compte.setTypeCompte(type);
            compte.setDateCreation(date);
            compte.setSolde(solde);
            compte.setClient(client);
            return compte;
        });
    }

    /**
     * Génère des comptes avec des champs potentiellement invalides
     */
    public static Arbitrary<Compte> comptesWithPotentiallyInvalidFields() {
        return Combinators.combine(
            ibansWithInvalid(),
            typesCompteWithInvalid(),
            datesCreationWithInvalid(),
            soldesWithInvalid(),
            ClientGenerators.clientsWithPotentiallyInvalidFields()
        ).as((iban, type, date, solde, client) -> {
            Compte compte = new Compte();
            compte.setNumeroCompte(iban);
            compte.setTypeCompte(type);
            compte.setDateCreation(date);
            compte.setSolde(solde);
            compte.setClient(client);
            return compte;
        });
    }

    // Générateurs pour champs valides
    private static Arbitrary<String> validIbans() {
        return Arbitraries.integers()
            .between(1, 1000)
            .map(i -> Iban.random(CountryCode.FR).toString());
    }

    private static Arbitrary<Compte.TypeCompte> typesCompte() {
        return Arbitraries.of(Compte.TypeCompte.values());
    }

    private static Arbitrary<LocalDate> datesCreation() {
        return Dates.dates()
            .between(LocalDate.of(2020, 1, 1), LocalDate.now());
    }

    private static Arbitrary<BigDecimal> soldesValides() {
        return Arbitraries.bigDecimals()
            .between(BigDecimal.ZERO, BigDecimal.valueOf(1000000))
            .ofScale(2);
    }

    // Générateurs avec valeurs potentiellement invalides
    private static Arbitrary<String> ibansWithInvalid() {
        return Arbitraries.oneOf(
            validIbans(),
            Arbitraries.strings().ofMaxLength(10), // Trop court
            Arbitraries.strings().withCharRange('a', 'z').ofLength(27), // Format invalide
            Arbitraries.just("INVALID_IBAN"), // Format complètement invalide
            Arbitraries.just(""), // Vide
            Arbitraries.just(null) // Null
        );
    }

    private static Arbitrary<Compte.TypeCompte> typesCompteWithInvalid() {
        return Arbitraries.oneOf(
            typesCompte(),
            Arbitraries.just(null) // Null
        );
    }

    private static Arbitrary<LocalDate> datesCreationWithInvalid() {
        return Arbitraries.oneOf(
            datesCreation(),
            Dates.dates().between(LocalDate.of(2050, 1, 1), LocalDate.of(2100, 1, 1)), // Future
            Arbitraries.just(null) // Null
        );
    }

    private static Arbitrary<BigDecimal> soldesWithInvalid() {
        return Arbitraries.oneOf(
            soldesValides(),
            Arbitraries.bigDecimals().between(BigDecimal.valueOf(-1000), BigDecimal.valueOf(-0.01)), // Négatif
            Arbitraries.just(null) // Null
        );
    }

    /**
     * Génère des montants valides pour les transactions
     */
    public static Arbitrary<BigDecimal> montantsTransactionValides() {
        return Arbitraries.bigDecimals()
            .between(BigDecimal.valueOf(0.01), BigDecimal.valueOf(10000))
            .ofScale(2);
    }

    /**
     * Génère des montants invalides pour tester la validation
     */
    public static Arbitrary<BigDecimal> montantsTransactionInvalides() {
        return Arbitraries.oneOf(
            Arbitraries.bigDecimals().between(BigDecimal.valueOf(-1000), BigDecimal.valueOf(-0.01)), // Négatif
            Arbitraries.just(BigDecimal.ZERO), // Zéro
            Arbitraries.just(null) // Null
        );
    }
}