package com.example.MeubleHub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    // Trouver les sous-catégories d'une catégorie parent
    List<Categorie> findByParentCategorieId(Long parentId);

    // Vérifier si une catégorie a des sous-catégories
    boolean existsByParentCategorieId(Long parentId);

    // Rechercher une catégorie par son nom (exact)
    Categorie findByNom(String nom);

    // Rechercher des catégories par nom (partiel)
    List<Categorie> findByNomContainingIgnoreCase(String nom);

    // Trouver toutes les catégories principales (sans parent)
    @Query("SELECT c FROM Categorie c WHERE c.parentCategorieId IS NULL")
    List<Categorie> findCategoriesPrincipales();
}