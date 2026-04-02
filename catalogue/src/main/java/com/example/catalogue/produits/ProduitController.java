package com.example.catalogue.produits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produit")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = produitService.allProduits();
        return new ResponseEntity<>(produits, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Produit produit = produitService.findById(id);
        if (produit != null) {
            return new ResponseEntity<>(produit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduit(@RequestBody Produit produit) {
        try {
            Produit newProduit = produitService.create(produit);
            return new ResponseEntity<>(newProduit, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la création du produit", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        produit.setId(id);
        try {
            Produit updatedProduit = produitService.update(produit);
            if (updatedProduit != null) {
                return new ResponseEntity<>(updatedProduit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Produit non trouvé", HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduit(@PathVariable Long id) {
        try {
            Produit existingProduit = produitService.findById(id);
            if (existingProduit != null) {
                produitService.delete(id);
                return new ResponseEntity<>("Produit supprimé avec succès", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Produit non trouvé", HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<Produit>> getProduitsByCategorie(@PathVariable Long categorieId) {
        List<Produit> produits = produitService.findByCategorieId(categorieId);
        return new ResponseEntity<>(produits, HttpStatus.OK);
    }
}
