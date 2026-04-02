package com.example.catalogue.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    List<Categorie> findByParentCategorieId(Long parentId);

    boolean existsByParentCategorieId(Long parentId);

    Categorie findByNom(String nom);

    List<Categorie> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT c FROM Categorie c WHERE c.parentCategorieId IS NULL")
    List<Categorie> findCategoriesPrincipales();
}
