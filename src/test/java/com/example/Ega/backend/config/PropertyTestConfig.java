package com.example.Ega.backend.config;

import net.jqwik.api.lifecycle.BeforeProperty;
import net.jqwik.api.lifecycle.AfterProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Configuration de base pour les tests basés sur les propriétés
 * Utilise une base de données MongoDB de test isolée
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.database=ega_bank_test",
    "spring.data.mongodb.host=localhost",
    "spring.data.mongodb.port=27017",
    "logging.level.org.springframework.data.mongodb=DEBUG",
    "logging.level.com.example.Ega.backend=DEBUG"
})
public abstract class PropertyTestConfig {

    @BeforeProperty
    void setupProperty() {
        // Configuration avant chaque propriété
        System.out.println("🧪 Démarrage d'un test de propriété");
    }

    @AfterProperty
    void cleanupProperty() {
        // Nettoyage après chaque propriété
        System.out.println("✅ Fin du test de propriété");
    }
}