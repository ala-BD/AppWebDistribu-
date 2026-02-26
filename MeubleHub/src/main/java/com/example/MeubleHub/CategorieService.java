package com.example.MeubleHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CategorieService implements ICategorie {

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Override
    public List<Categorie> allCategories() {
        return categorieRepository.findAll();
    }

    @Override
    public Categorie findById(Long id) {
        Optional<Categorie> categorie = categorieRepository.findById(id);
        return categorie.orElse(null);
    }

    @Override
    @Transactional
    public Categorie create(Categorie c) {
        // Vérifier si le nom est unique
        if (categorieRepository.findByNom(c.getNom()) != null) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        // Vérifier si la catégorie parente existe (si spécifiée)
        if (c.getParentCategorieId() != null) {
            if (!categorieRepository.existsById(c.getParentCategorieId())) {
                throw new RuntimeException("La catégorie parente n'existe pas");
            }
        }

        return categorieRepository.save(c);
    }

    @Override
    @Transactional
    public Categorie update(Categorie c) {
        if (c.getId() == null || !categorieRepository.existsById(c.getId())) {
            return null;
        }

        // Vérifier l'unicité du nom (sauf si c'est le même)
        Categorie existingWithSameName = categorieRepository.findByNom(c.getNom());
        if (existingWithSameName != null && !existingWithSameName.getId().equals(c.getId())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        // Vérifier si la catégorie parente existe (si spécifiée)
        if (c.getParentCategorieId() != null) {
            if (!categorieRepository.existsById(c.getParentCategorieId())) {
                throw new RuntimeException("La catégorie parente n'existe pas");
            }

            // Empêcher de se définir soi-même comme parent
            if (c.getParentCategorieId().equals(c.getId())) {
                throw new RuntimeException("Une catégorie ne peut pas être son propre parent");
            }
        }

        return categorieRepository.save(c);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id != null && categorieRepository.existsById(id)) {
            // Vérifier si la catégorie a des sous-catégories
            if (categorieRepository.existsByParentCategorieId(id)) {
                throw new RuntimeException("Impossible de supprimer une catégorie qui a des sous-catégories");
            }

            // Vérifier si la catégorie a des produits
            List<Produit> produits = produitRepository.findByCategorieId(id);
            if (produits != null && !produits.isEmpty()) {
                throw new RuntimeException("Impossible de supprimer une catégorie qui contient des produits");
            }

            categorieRepository.deleteById(id);
        }
    }

    @Override
    public List<Categorie> findSousCategories(Long parentId) {
        return categorieRepository.findByParentCategorieId(parentId);
    }

    // Méthodes supplémentaires

    public List<Categorie> getCategoriesPrincipales() {
        return categorieRepository.findCategoriesPrincipales();
    }

    public List<Categorie> rechercherParNom(String nom) {
        return categorieRepository.findByNomContainingIgnoreCase(nom);
    }

    @Transactional
    public Categorie ajouterSousCategorie(Long parentId, Categorie sousCategorie) {
        Categorie parent = findById(parentId);
        if (parent == null) {
            throw new RuntimeException("Catégorie parente non trouvée");
        }

        sousCategorie.setParentCategorieId(parentId);
        return create(sousCategorie);
    }
}