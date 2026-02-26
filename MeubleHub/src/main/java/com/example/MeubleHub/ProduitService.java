package com.example.MeubleHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProduitService implements IProduit {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Override
    public List<Produit> allProduits() {
        return produitRepository.findAll();
    }

    @Override
    public Produit findById(Long id) {
        Optional<Produit> produit = produitRepository.findById(id);
        return produit.orElse(null);
    }

    @Override
    @Transactional
    public Produit create(Produit p) {
        // Vérifier que la catégorie existe si elle est spécifiée
        if (p.getCategorie() != null && p.getCategorie().getId() != null) {
            Optional<Categorie> categorie = categorieRepository.findById(p.getCategorie().getId());
            if (categorie.isEmpty()) {
                throw new RuntimeException("La catégorie spécifiée n'existe pas");
            }
            p.setCategorie(categorie.get());
        } else if (p.getCategorie() != null && p.getCategorie().getId() == null) {
            // Si une catégorie est fournie sans ID, on la sauvegarde d'abord
            Categorie savedCategorie = categorieRepository.save(p.getCategorie());
            p.setCategorie(savedCategorie);
        }

        if (p.getDateAjout() == null) {
            p.setDateAjout(java.time.LocalDateTime.now());
        }
        return produitRepository.save(p);
    }

    @Override
    @Transactional
    public Produit update(Produit p) {
        if (p.getId() == null || !produitRepository.existsById(p.getId())) {
            return null;
        }

        // Récupérer le produit existant
        Produit existingProduit = produitRepository.findById(p.getId()).orElse(null);
        if (existingProduit == null) {
            return null;
        }

        // Mettre à jour les champs
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

        // Gérer la catégorie
        if (p.getCategorie() != null) {
            if (p.getCategorie().getId() != null) {
                Optional<Categorie> categorie = categorieRepository.findById(p.getCategorie().getId());
                if (categorie.isEmpty()) {
                    throw new RuntimeException("La catégorie spécifiée n'existe pas");
                }
                existingProduit.setCategorie(categorie.get());
            } else {
                // Nouvelle catégorie
                Categorie savedCategorie = categorieRepository.save(p.getCategorie());
                existingProduit.setCategorie(savedCategorie);
            }
        }

        return produitRepository.save(existingProduit);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id != null && produitRepository.existsById(id)) {
            produitRepository.deleteById(id);
        }
    }

    // Méthode supplémentaire pour trouver les produits par catégorie
    public List<Produit> findByCategorieId(Long categorieId) {
        return produitRepository.findByCategorieId(categorieId);
    }
}