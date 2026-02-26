package com.example.MeubleHub;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "parent_categorie_id")
    private Long parentCategorieId;

    // Relation avec les produits
    @OneToMany(mappedBy = "categorie", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Produit> produits;

    // Relation avec les sous-catégories (optionnel)
    @Transient
    private List<Categorie> sousCategories;
}