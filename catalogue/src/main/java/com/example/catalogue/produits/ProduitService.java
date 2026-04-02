package com.example.catalogue.produits;

import com.example.catalogue.categories.Categorie;
import com.example.catalogue.categories.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProduitService implements IProduit {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    /**
     * With open-in-view=false, JSON serialization runs after the transaction ends; touch lazy
     * {@code @ElementCollection} maps inside a read transaction so GET /produit does not 500.
     */
    private static void touchElementCollections(Produit p) {
        if (p == null) {
            return;
        }
        if (p.getImages() != null) {
            p.getImages().size();
        }
        if (p.getCaracteristiques() != null) {
            p.getCaracteristiques().size();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> allProduits() {
        List<Produit> list = produitRepository.findAll();
        list.forEach(ProduitService::touchElementCollections);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Produit findById(Long id) {
        Optional<Produit> produit = produitRepository.findById(id);
        Produit p = produit.orElse(null);
        touchElementCollections(p);
        return p;
    }

    @Override
    @Transactional
    public Produit create(Produit p) {
        if (p.getCategorie() != null && p.getCategorie().getId() != null) {
            Optional<Categorie> categorie = categorieRepository.findById(p.getCategorie().getId());
            if (categorie.isEmpty()) {
                throw new RuntimeException("La catégorie spécifiée n'existe pas");
            }
            p.setCategorie(categorie.get());
        } else if (p.getCategorie() != null && p.getCategorie().getId() == null) {
            Categorie savedCategorie = categorieRepository.save(p.getCategorie());
            p.setCategorie(savedCategorie);
        }

        if (p.getDateAjout() == null) {
            p.setDateAjout(java.time.LocalDateTime.now());
        }
        if (p.getImages() == null) {
            p.setImages(new ArrayList<>());
        }
        if (p.getCaracteristiques() == null) {
            p.setCaracteristiques(new HashMap<>());
        }
        Produit saved = produitRepository.save(p);
        touchElementCollections(saved);
        return saved;
    }

    @Override
    @Transactional
    public Produit update(Produit p) {
        if (p.getId() == null || !produitRepository.existsById(p.getId())) {
            return null;
        }

        Produit existingProduit = produitRepository.findById(p.getId()).orElse(null);
        if (existingProduit == null) {
            return null;
        }

        if (p.getNom() != null) {
            existingProduit.setNom(p.getNom());
        }
        if (p.getDescription() != null) {
            existingProduit.setDescription(p.getDescription());
        }
        if (p.getPrix() != null) {
            existingProduit.setPrix(p.getPrix());
        }
        if (p.getQuantiteStock() != null) {
            existingProduit.setQuantiteStock(p.getQuantiteStock());
        }
        if (p.getVendeurId() != null) {
            existingProduit.setVendeurId(p.getVendeurId());
        }
        if (p.getImages() != null) {
            existingProduit.setImages(p.getImages());
        }
        if (p.getCaracteristiques() != null) {
            existingProduit.setCaracteristiques(p.getCaracteristiques());
        }

        if (p.getCategorie() != null) {
            if (p.getCategorie().getId() != null) {
                Optional<Categorie> categorie = categorieRepository.findById(p.getCategorie().getId());
                if (categorie.isEmpty()) {
                    throw new RuntimeException("La catégorie spécifiée n'existe pas");
                }
                existingProduit.setCategorie(categorie.get());
            } else {
                Categorie savedCategorie = categorieRepository.save(p.getCategorie());
                existingProduit.setCategorie(savedCategorie);
            }
        }

        Produit saved = produitRepository.save(existingProduit);
        touchElementCollections(saved);
        return saved;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id != null && produitRepository.existsById(id)) {
            produitRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public List<Produit> findByCategorieId(Long categorieId) {
        List<Produit> list = produitRepository.findByCategorieId(categorieId);
        list.forEach(ProduitService::touchElementCollections);
        return list;
    }
}
