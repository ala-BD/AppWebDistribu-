package com.example.catalogue.produits;

import java.util.List;

public interface IProduit {
    List<Produit> allProduits();
    Produit findById(Long id);
    Produit create(Produit p);
    Produit update(Produit p);
    void delete(Long id);
}
