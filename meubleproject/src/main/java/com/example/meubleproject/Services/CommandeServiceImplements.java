package com.example.meubleproject.Services;

import com.example.meubleproject.Entities.Commande;
import com.example.meubleproject.Repositories.CommandeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeServiceImplements implements IServiceCommande {

    private final CommandeRepo commandeRepo;

    public CommandeServiceImplements(CommandeRepo commandeRepo) {
        this.commandeRepo = commandeRepo;
    }

    @Override
    public Commande create(Commande commande) {
        return commandeRepo.save(commande);
    }

    @Override
    public Commande update(Long id, Commande commande) {
        Commande existing = commandeRepo.findById(id).orElseThrow(() -> new RuntimeException("Commande not found"));
        existing.setNumeroCommande(commande.getNumeroCommande());
        existing.setClientId(commande.getClientId());
        existing.setDateCommande(commande.getDateCommande());
        existing.setStatut(commande.getStatut());
        existing.setTotal(commande.getTotal());
        existing.setAdresseLivraison(commande.getAdresseLivraison());
        existing.setDateLivraisonPrevue(commande.getDateLivraisonPrevue());
        existing.setLignesCommande(commande.getLignesCommande());
        existing.setLivraison(commande.getLivraison());
        return commandeRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        commandeRepo.deleteById(id);
    }

    @Override
    public Optional<Commande> getById(Long id) {
        return commandeRepo.findById(id);
    }

    @Override
    public Optional<Commande> getByNumeroCommande(String numeroCommande) {
        return commandeRepo.findByNumeroCommande(numeroCommande);
    }

    @Override
    public List<Commande> getAll() {
        return commandeRepo.findAll();
    }
}