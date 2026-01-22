package com.example.Ega.backend.generators;

import com.example.Ega.backend.entity.Client;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.time.api.Dates;

import java.time.LocalDate;

/**
 * Générateurs de données pour les tests basés sur les propriétés des entités Client
 */
public class ClientGenerators {

    /**
     * Génère des clients valides avec des données réalistes
     */
    public static Arbitrary<Client> validClients() {
        return Combinators.combine(
            noms(),
            prenoms(),
            datesNaissance(),
            sexes(),
            adresses(),
            telephones(),
            emails(),
            nationalites()
        ).as((nom, prenom, dateNaissance, sexe, adresse, telephone, email, nationalite) -> {
            Client client = new Client();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setDateNaissance(dateNaissance);
            client.setSexe(sexe);
            client.setAdresse(adresse);
            client.setTelephone(telephone);
            client.setCourriel(email);
            client.setNationalite(nationalite);
            return client;
        });
    }

    /**
     * Génère des clients avec des champs potentiellement invalides pour tester la validation
     */
    public static Arbitrary<Client> clientsWithPotentiallyInvalidFields() {
        return Combinators.combine(
            nomsWithInvalid(),
            prenomsWithInvalid(),
            datesNaissanceWithInvalid(),
            sexesWithInvalid(),
            adressesWithInvalid(),
            telephonesWithInvalid(),
            emailsWithInvalid(),
            nationalitesWithInvalid()
        ).as((nom, prenom, dateNaissance, sexe, adresse, telephone, email, nationalite) -> {
            Client client = new Client();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setDateNaissance(dateNaissance);
            client.setSexe(sexe);
            client.setAdresse(adresse);
            client.setTelephone(telephone);
            client.setCourriel(email);
            client.setNationalite(nationalite);
            return client;
        });
    }

    // Générateurs pour champs valides
    private static Arbitrary<String> noms() {
        return Arbitraries.of("Dupont", "Martin", "Bernard", "Durand", "Moreau", "Laurent", "Simon", "Michel", "Garcia", "David");
    }

    private static Arbitrary<String> prenoms() {
        return Arbitraries.of("Jean", "Marie", "Pierre", "Michel", "André", "Philippe", "Alain", "Bernard", "Robert", "Jacques");
    }

    private static Arbitrary<LocalDate> datesNaissance() {
        return Dates.dates()
            .between(LocalDate.of(1920, 1, 1), LocalDate.of(2005, 12, 31));
    }

    private static Arbitrary<String> sexes() {
        return Arbitraries.of("M", "F");
    }

    private static Arbitrary<String> adresses() {
        return Arbitraries.strings()
            .withCharRange('a', 'z')
            .withCharRange('A', 'Z')
            .withCharRange('0', '9')
            .withChars(' ', ',', '-')
            .ofMinLength(10)
            .ofMaxLength(100)
            .map(s -> s.trim().isEmpty() ? "123 Rue de la Paix, 75001 Paris" : s);
    }

    private static Arbitrary<String> telephones() {
        return Arbitraries.strings()
            .withCharRange('0', '9')
            .ofLength(10)
            .map(s -> s.startsWith("0") ? s : "0" + s.substring(1));
    }

    private static Arbitrary<String> emails() {
        return Combinators.combine(
            Arbitraries.strings().withCharRange('a', 'z').ofMinLength(3).ofMaxLength(10),
            Arbitraries.of("gmail.com", "yahoo.fr", "hotmail.com", "outlook.fr", "free.fr")
        ).as((user, domain) -> user + "@" + domain);
    }

    private static Arbitrary<String> nationalites() {
        return Arbitraries.of("Française", "Belge", "Suisse", "Canadienne", "Allemande", "Italienne", "Espagnole");
    }

    // Générateurs avec valeurs potentiellement invalides
    private static Arbitrary<String> nomsWithInvalid() {
        return Arbitraries.oneOf(
            noms(),
            Arbitraries.strings().ofMaxLength(2), // Trop court
            Arbitraries.just(""), // Vide
            Arbitraries.just(null) // Null
        );
    }

    private static Arbitrary<String> prenomsWithInvalid() {
        return Arbitraries.oneOf(
            prenoms(),
            Arbitraries.strings().ofMaxLength(1), // Trop court
            Arbitraries.just(""), // Vide
            Arbitraries.just(null) // Null
        );
    }

    private static Arbitrary<LocalDate> datesNaissanceWithInvalid() {
        return Arbitraries.oneOf(
            datesNaissance(),
            Dates.dates().between(LocalDate.of(2020, 1, 1), LocalDate.now()), // Trop récent
            Dates.dates().between(LocalDate.of(1800, 1, 1), LocalDate.of(1900, 1, 1)) // Trop ancien
        );
    }

    private static Arbitrary<String> sexesWithInvalid() {
        return Arbitraries.oneOf(
            sexes(),
            Arbitraries.of("X", "O", ""), // Valeurs invalides
            Arbitraries.just(null)
        );
    }

    private static Arbitrary<String> adressesWithInvalid() {
        return Arbitraries.oneOf(
            adresses(),
            Arbitraries.strings().ofMaxLength(5), // Trop court
            Arbitraries.just(""), // Vide
            Arbitraries.just(null) // Null
        );
    }

    private static Arbitrary<String> telephonesWithInvalid() {
        return Arbitraries.oneOf(
            telephones(),
            Arbitraries.strings().ofLength(5), // Trop court
            Arbitraries.strings().withCharRange('a', 'z').ofLength(10), // Lettres
            Arbitraries.just(""), // Vide
            Arbitraries.just(null) // Null
        );
    }

    private static Arbitrary<String> emailsWithInvalid() {
        return Arbitraries.oneOf(
            emails(),
            Arbitraries.strings().ofMaxLength(5), // Pas d'@
            Arbitraries.just("invalid-email"), // Format invalide
            Arbitraries.just("@domain.com"), // Pas de partie locale
            Arbitraries.just("user@"), // Pas de domaine
            Arbitraries.just(""), // Vide
            Arbitraries.just(null) // Null
        );
    }

    private static Arbitrary<String> nationalitesWithInvalid() {
        return Arbitraries.oneOf(
            nationalites(),
            Arbitraries.strings().ofMaxLength(2), // Trop court
            Arbitraries.just(""), // Vide
            Arbitraries.just(null) // Null
        );
    }
}