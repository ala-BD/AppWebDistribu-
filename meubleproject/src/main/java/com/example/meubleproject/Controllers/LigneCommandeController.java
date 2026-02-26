package com.example.meubleproject.Controllers;

import com.example.meubleproject.Entities.LigneCommande;
import com.example.meubleproject.Services.IServiceLignCommande;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lignes-commandes")
public class LigneCommandeController {

    private final IServiceLignCommande ligneCommandeService;

    public LigneCommandeController(IServiceLignCommande ligneCommandeService) {
        this.ligneCommandeService = ligneCommandeService;
    }

    @PostMapping
    public LigneCommande create(@RequestBody LigneCommande ligneCommande) {
        return ligneCommandeService.create(ligneCommande);
    }

    @PutMapping("/{id}")
    public LigneCommande update(@PathVariable Long id, @RequestBody LigneCommande ligneCommande) {
        return ligneCommandeService.update(id, ligneCommande);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ligneCommandeService.delete(id);
    }

    @GetMapping("/{id}")
    public Optional<LigneCommande> getById(@PathVariable Long id) {
        return ligneCommandeService.getById(id);
    }

    @GetMapping("/commande/{commandeId}")
    public List<LigneCommande> getByCommandeId(@PathVariable Long commandeId) {
        return ligneCommandeService.getByCommandeId(commandeId);
    }

    @GetMapping
    public List<LigneCommande> getAll() {
        return ligneCommandeService.getAll();
    }

    @DeleteMapping("/commande/{commandeId}")
    public void deleteByCommandeId(@PathVariable Long commandeId) {
        ligneCommandeService.deleteByCommandeId(commandeId);
    }
}