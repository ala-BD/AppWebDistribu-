package com.example.meubleproject.Controllers;

import com.example.meubleproject.Entities.Livraison;
import com.example.meubleproject.Services.IServiceLivraison;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livraisons")
public class LivraisonController {

    private final IServiceLivraison livraisonService;

    public LivraisonController(IServiceLivraison livraisonService) {
        this.livraisonService = livraisonService;
    }

    @PostMapping
    public Livraison create(@RequestBody Livraison livraison) {
        return livraisonService.create(livraison);
    }

    @PutMapping("/{id}")
    public Livraison update(@PathVariable Long id, @RequestBody Livraison livraison) {
        return livraisonService.update(id, livraison);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        livraisonService.delete(id);
    }

    @GetMapping("/{id}")
    public Optional<Livraison> getById(@PathVariable Long id) {
        return livraisonService.getById(id);
    }

    @GetMapping("/commande/{commandeId}")
    public Optional<Livraison> getByCommandeId(@PathVariable Long commandeId) {
        return livraisonService.getByCommandeId(commandeId);
    }

    @GetMapping
    public List<Livraison> getAll() {
        return livraisonService.getAll();
    }
}