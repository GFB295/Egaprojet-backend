# Configuration MongoDB pour Ega Bank

## Nom de la base de données
**Nom de la base de données : `ega_bank`**

## Configuration actuelle
L'application est configurée pour se connecter à MongoDB avec les paramètres suivants :

- **URI de connexion** : `mongodb://localhost:27017/ega_bank`
- **Host** : `localhost`
- **Port** : `27017`
- **Base de données** : `ega_bank`

## Installation et démarrage de MongoDB

### Option 1 : MongoDB Community Server (Recommandé)
1. Téléchargez MongoDB Community Server depuis : https://www.mongodb.com/try/download/community
2. Installez MongoDB
3. Démarrez le service MongoDB :
   - **Windows** : Le service démarre automatiquement après l'installation
   - **Linux/Mac** : `sudo systemctl start mongod` ou `brew services start mongodb-community`

### Option 2 : MongoDB avec Docker
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### Option 3 : MongoDB Atlas (Cloud)
Si vous utilisez MongoDB Atlas, modifiez `application.properties` :
```properties
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/ega_bank
```

## Vérification de la connexion

### Vérifier que MongoDB est en cours d'exécution
```bash
# Windows PowerShell
Get-Service MongoDB

# Linux/Mac
sudo systemctl status mongod
```

### Se connecter à MongoDB avec le shell
```bash
mongosh
# ou
mongo
```

### Vérifier les bases de données
```javascript
show dbs
```

Vous devriez voir `ega_bank` après le premier démarrage de l'application.

## Collections créées automatiquement

L'application créera automatiquement les collections suivantes dans `ega_bank` :
- `clients` - Informations des clients
- `comptes` - Comptes bancaires
- `transactions` - Historique des transactions
- `users` - Utilisateurs pour l'authentification

## Dépannage

### MongoDB n'est pas accessible
1. Vérifiez que MongoDB est en cours d'exécution
2. Vérifiez que le port 27017 n'est pas bloqué par un firewall
3. Vérifiez les logs MongoDB pour les erreurs

### L'application ne peut pas se connecter
1. Vérifiez que l'URI dans `application.properties` est correcte
2. Vérifiez que MongoDB écoute sur le port 27017
3. Vérifiez les logs de l'application Spring Boot
