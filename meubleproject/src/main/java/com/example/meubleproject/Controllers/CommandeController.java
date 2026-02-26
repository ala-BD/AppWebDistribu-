package com.example.meubleproject.Controllers;

import com.example.meubleproject.Entities.Commande;
import com.example.meubleproject.Services.IServiceCommande;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final IServiceCommande commandeService;

    public CommandeController(IServiceCommande commandeService) {
        this.commandeService = commandeService;
    }

    @PostMapping("/addCommande")
    public Commande create(@RequestBody Commande commande) {
        return commandeService.create(commande);
    }

    @PutMapping("/{id}")
    public Commande update(@PathVariable Long id, @RequestBody Commande commande) {
        return commandeService.update(id, commande);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commandeService.delete(id);
    }

    @GetMapping("/{id}")
    public Optional<Commande> getById(@PathVariable Long id) {
        return commandeService.getById(id);
    }

    @GetMapping("/numero/{numeroCommande}")
    public Optional<Commande> getByNumeroCommande(@PathVariable String numeroCommande) {
        return commandeService.getByNumeroCommande(numeroCommande);
    }

    @GetMapping
    public List<Commande> getAll() {
        return commandeService.getAll();
    }
}