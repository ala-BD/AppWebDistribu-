package com.example.MeubleHub;

import java.util.List;

public interface ICategorie {
    List<Categorie> allCategories();
    Categorie findById(Long id);
    Categorie create(Categorie c);
    Categorie update(Categorie c);
    void delete(Long id);
    List<Categorie> findSousCategories(Long parentId); // Méthode supplémentaire pour trouver les sous-catégories
}