# Guide d'utilisation de la collection Postman - MeubleHub API

## 📥 Importation de la collection

1. Ouvrez Postman
2. Cliquez sur **Import** (en haut à gauche)
3. Sélectionnez le fichier `MeubleHub_Postman_Collection.json`
4. La collection apparaîtra dans votre workspace

## 🔧 Configuration

### Variable d'environnement
La collection utilise une variable `base_url` définie à `http://localhost:8095`

Si votre application tourne sur un autre port, modifiez la variable dans Postman :
- Cliquez sur la collection → Onglet **Variables**
- Modifiez `base_url` si nécessaire

## 📋 Endpoints disponibles

### 🏷️ Catégorie CRUD

#### 1. GET - Toutes les catégories
```
GET http://localhost:8095/categorie
```
**Réponse attendue :** Liste de toutes les catégories

#### 2. GET - Catégorie par ID
```
GET http://localhost:8095/categorie/{id}
```
**Exemple :** `GET http://localhost:8095/categorie/1`

#### 3. POST - Créer une catégorie
```
POST http://localhost:8095/categorie
Content-Type: application/json

{
    "nom": "Meubles de salon",
    "description": "Catégorie pour tous les meubles de salon"
}
```

#### 4. PUT - Mettre à jour une catégorie
```
PUT http://localhost:8095/categorie/{id}
Content-Type: application/json

{
    "nom": "Meubles de salon - Modifié",
    "description": "Description mise à jour"
}
```

#### 5. DELETE - Supprimer une catégorie
```
DELETE http://localhost:8095/categorie/{id}
```
**Note :** Ne fonctionne que si la catégorie n'a pas de produits associés

#### 6. GET - Catégories principales
```
GET http://localhost:8095/categorie/principales
```
Récupère les catégories sans parent

#### 7. GET - Rechercher catégories
```
GET http://localhost:8095/categorie/recherche?nom=salon
```

---

### 📦 Produit CRUD

#### 1. GET - Tous les produits
```
GET http://localhost:8095/produit
```

#### 2. GET - Produit par ID
```
GET http://localhost:8095/produit/{id}
```

#### 3. POST - Créer un produit (avec catégorie)
```
POST http://localhost:8095/produit
Content-Type: application/json

{
    "nom": "Chaise en bois massif",
    "description": "Belle chaise en bois massif, confortable et élégante",
    "prix": 250.0,
    "quantiteStock": 15,
    "vendeurId": 1,
    "categorie": {
        "id": 1
    },
    "images": [
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg"
    ],
    "caracteristiques": {
        "matiere": "Bois massif",
        "couleur": "Chêne naturel",
        "dimensions": "45x50x90 cm"
    }
}
```

**Important :** La catégorie doit exister avant de créer le produit. Utilisez d'abord `POST /categorie` pour créer une catégorie.

#### 4. PUT - Mettre à jour un produit
```
PUT http://localhost:8095/produit/{id}
Content-Type: application/json

{
    "nom": "Chaise en bois massif - Modifiée",
    "description": "Description mise à jour",
    "prix": 280.0,
    "quantiteStock": 20,
    "categorie": {
        "id": 1
    }
}
```

#### 5. DELETE - Supprimer un produit
```
DELETE http://localhost:8095/produit/{id}
```

#### 6. GET - Produits par catégorie
```
GET http://localhost:8095/produit/categorie/{categorieId}
```
Récupère tous les produits d'une catégorie spécifique

---

## 🎯 Scénarios de test recommandés

### Scénario 1 : Workflow complet
1. **Créer une catégorie** → `POST /categorie`
2. **Créer un produit** avec cette catégorie → `POST /produit`
3. **Récupérer les produits** de cette catégorie → `GET /produit/categorie/{id}`
4. **Mettre à jour le produit** → `PUT /produit/{id}`
5. **Supprimer le produit** → `DELETE /produit/{id}`
6. **Supprimer la catégorie** → `DELETE /categorie/{id}`

### Scénario 2 : Test de contraintes
1. Créer une catégorie
2. Créer un produit dans cette catégorie
3. Essayer de supprimer la catégorie → **Devrait échouer** avec message d'erreur
4. Supprimer d'abord le produit
5. Supprimer la catégorie → **Devrait réussir**

---

## ⚠️ Codes de réponse HTTP

- **200 OK** : Requête réussie
- **201 Created** : Ressource créée avec succès
- **400 Bad Request** : Erreur de validation (ex: catégorie inexistante, nom déjà utilisé)
- **404 Not Found** : Ressource non trouvée
- **500 Internal Server Error** : Erreur serveur

---

## 📝 Exemples de réponses

### Réponse succès - Création catégorie
```json
{
    "id": 1,
    "nom": "Meubles de salon",
    "description": "Catégorie pour tous les meubles de salon",
    "parentCategorieId": null,
    "produits": null
}
```

### Réponse succès - Création produit
```json
{
    "id": 1,
    "nom": "Chaise en bois massif",
    "description": "Belle chaise en bois massif, confortable et élégante",
    "prix": 250.0,
    "quantiteStock": 15,
    "vendeurId": 1,
    "dateAjout": "2024-02-15T10:30:00",
    "categorie": {
        "id": 1,
        "nom": "Meubles de salon",
        "description": "Catégorie pour tous les meubles de salon"
    },
    "images": ["https://example.com/image1.jpg"],
    "caracteristiques": {
        "matiere": "Bois massif",
        "couleur": "Chêne naturel"
    }
}
```

### Réponse erreur - Suppression catégorie avec produits
```json
"Impossible de supprimer une catégorie qui contient des produits"
```

---

## 🚀 Ordre recommandé pour tester

1. Commencez par créer des catégories
2. Ensuite, créez des produits en utilisant les IDs des catégories créées
3. Testez les mises à jour
4. Testez les suppressions (produits d'abord, puis catégories)

---

## 💡 Astuces

- **Sauvegardez les IDs** : Après avoir créé une catégorie, notez son ID pour l'utiliser lors de la création de produits
- **Utilisez les variables Postman** : Vous pouvez stocker l'ID d'une catégorie créée dans une variable Postman pour l'utiliser dans les requêtes suivantes
- **Vérifiez les relations** : Utilisez `GET /produit/categorie/{id}` pour vérifier que les produits sont bien associés à leur catégorie

