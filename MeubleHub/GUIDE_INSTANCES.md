# Guide de test avec plusieurs instances - MeubleHub

## 🎯 Recommandation

**Testez d'abord sur le projet initial (port 8095)** car c'est celui qui contient toutes les corrections et la relation Categorie-Produit fonctionnelle.

## 📊 Configuration des instances

### Instance 1 (Projet initial) - Port 8095
- **Fichier de config** : `application.properties`
- **Port** : 8095
- **Base de données** : `./Database/Data/Candidat`
- **Status** : ✅ **RECOMMANDÉ pour les tests** - Contient toutes les corrections

### Instance 2 - Port 8096
- **Fichier de config** : `application-instance2.properties`
- **Port** : 8096
- **Base de données** : `./Database/Data/Candidat_Instance2`
- **Pour lancer** : `--spring.profiles.active=instance2`

### Instance 3 - Port 8097
- **Fichier de config** : `application-instance3.properties`
- **Port** : 8097
- **Base de données** : `./Database/Data/Candidat_Instance3`
- **Pour lancer** : `--spring.profiles.active=instance3`

## 🚀 Comment lancer les instances

### Option 1 : Via IntelliJ IDEA

#### Instance 1 (Initial - Port 8095)
1. Clic droit sur `MeubleHubApplication.java`
2. **Run 'MeubleHubApplication'**
3. ✅ Utilise `application.properties` par défaut

#### Instance 2 (Port 8096)
1. Clic droit sur `MeubleHubApplication.java`
2. **Run 'MeubleHubApplication'** → **Edit Configurations...**
3. Dans **VM options** ou **Program arguments**, ajoutez :
   ```
   --spring.profiles.active=instance2
   ```
4. Ou dans **Active profiles** : `instance2`
5. Cliquez **OK** et lancez

#### Instance 3 (Port 8097)
Même procédure avec `--spring.profiles.active=instance3`

### Option 2 : Via ligne de commande

#### Instance 1 (Initial)
```bash
mvnw spring-boot:run
# ou
java -jar target/MeubleHub-0.0.1-SNAPSHOT.jar
```

#### Instance 2
```bash
mvnw spring-boot:run -Dspring-boot.run.profiles=instance2
# ou
java -jar target/MeubleHub-0.0.1-SNAPSHOT.jar --spring.profiles.active=instance2
```

#### Instance 3
```bash
mvnw spring-boot:run -Dspring-boot.run.profiles=instance3
# ou
java -jar target/MeubleHub-0.0.1-SNAPSHOT.jar --spring.profiles.active=instance3
```

## 🧪 Tests Postman

### Pour tester l'instance 1 (Initial - RECOMMANDÉ)
```
http://localhost:8095/categorie
http://localhost:8095/produit
```

### Pour tester l'instance 2
```
http://localhost:8096/categorie
http://localhost:8096/produit
```

### Pour tester l'instance 3
```
http://localhost:8097/categorie
http://localhost:8097/produit
```

## ⚠️ Points importants

1. **Base de données séparée** : Chaque instance a sa propre base de données H2
   - Instance 1 : `Candidat.mv.db`
   - Instance 2 : `Candidat_Instance2.mv.db`
   - Instance 3 : `Candidat_Instance3.mv.db`

2. **Données isolées** : Les données créées dans une instance ne sont pas visibles dans les autres

3. **Eureka** : Si Eureka est lancé, toutes les instances s'enregistreront automatiquement

4. **Pour les tests CRUD** : Utilisez l'instance 1 (port 8095) car c'est celle qui a toutes les corrections

## 📝 Ordre recommandé pour tester

1. ✅ **Lancez d'abord Eureka** (si vous l'utilisez) sur le port 8761
2. ✅ **Lancez l'instance 1** (port 8095) - **C'est celle à tester en priorité**
3. ✅ **Testez tous les CRUD** avec Postman sur `http://localhost:8095`
4. ⚙️ Si besoin, lancez les instances 2 et 3 pour tester la scalabilité

## 🔍 Vérification

Pour vérifier quelle instance tourne, regardez les logs au démarrage :
```
Started MeubleHubApplication in X seconds
Tomcat started on port(s): 8095 (http)
```

Ou testez directement :
```bash
curl http://localhost:8095/actuator/health
curl http://localhost:8096/actuator/health
curl http://localhost:8097/actuator/health
```

