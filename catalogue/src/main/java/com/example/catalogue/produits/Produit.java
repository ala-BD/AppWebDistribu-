package com.example.catalogue.produits;

import com.example.catalogue.categories.Categorie;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double prix;

    @Column(name = "quantite_stock", nullable = false)
    private Integer quantiteStock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "produits"})
    private Categorie categorie;

    @Column(name = "vendeur_id")
    private Long vendeurId;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    @ElementCollection
    private List<String> images;

    @ElementCollection
    private Map<String, String> caracteristiques;

    @PrePersist
    protected void onCreate() {
        dateAjout = LocalDateTime.now();
    }
}
