package com.example.MeubleHub;

import java.util.List;

public interface IProduit {
    List<Produit> allProduits();
    Produit findById(Long id);  // Changé de long à Long
    Produit create(Produit p);
    Produit update(Produit p);
    void delete(Long id);  // Gardé Long
}