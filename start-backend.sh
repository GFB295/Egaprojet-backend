#!/bin/bash
echo "========================================"
echo "  Démarrage de Ega Bank Backend"
echo "========================================"
echo ""
echo "Base de données MongoDB: ega_bank"
echo "URI: mongodb://localhost:27017/ega_bank"
echo ""
echo "Vérifiez que MongoDB est en cours d'exécution avant de continuer..."
echo ""
read -p "Appuyez sur Entrée pour continuer..."
echo ""
echo "Démarrage de l'application Spring Boot..."
./mvnw spring-boot:run
