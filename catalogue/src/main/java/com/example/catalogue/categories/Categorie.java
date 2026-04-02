package com.example.catalogue.categories;

import com.example.catalogue.produits.Produit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @OneToMany(mappedBy = "categorie", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Produit> produits;

    @Transient
    private List<Categorie> sousCategories;
}
