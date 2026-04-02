package com.example.catalogue.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorie")
public class CategorieController {

    @Autowired
    private CategorieService categorieService;

    @GetMapping
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = categorieService.allCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable Long id) {
        Categorie categorie = categorieService.findById(id);
        if (categorie != null) {
            return new ResponseEntity<>(categorie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategorie(@RequestBody Categorie categorie) {
        try {
            Categorie newCategorie = categorieService.create(categorie);
            return new ResponseEntity<>(newCategorie, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la création", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategorie(@PathVariable Long id, @RequestBody Categorie categorie) {
        categorie.setId(id);
        try {
            Categorie updatedCategorie = categorieService.update(categorie);
            if (updatedCategorie != null) {
                return new ResponseEntity<>(updatedCategorie, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Catégorie non trouvée", HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategorie(@PathVariable Long id) {
        try {
            Categorie existingCategorie = categorieService.findById(id);
            if (existingCategorie != null) {
                categorieService.delete(id);
                return new ResponseEntity<>("Catégorie supprimée avec succès", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Catégorie non trouvée", HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/sous-categories")
    public ResponseEntity<List<Categorie>> getSousCategories(@PathVariable Long id) {
        List<Categorie> sousCategories = categorieService.findSousCategories(id);
        return new ResponseEntity<>(sousCategories, HttpStatus.OK);
    }

    @GetMapping("/principales")
    public ResponseEntity<List<Categorie>> getCategoriesPrincipales() {
        List<Categorie> categories = categorieService.getCategoriesPrincipales();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<Categorie>> rechercherCategories(@RequestParam String nom) {
        List<Categorie> categories = categorieService.rechercherParNom(nom);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/{parentId}/sous-categorie")
    public ResponseEntity<?> ajouterSousCategorie(
            @PathVariable Long parentId,
            @RequestBody Categorie sousCategorie) {
        try {
            Categorie newCategorie = categorieService.ajouterSousCategorie(parentId, sousCategorie);
            return new ResponseEntity<>(newCategorie, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
