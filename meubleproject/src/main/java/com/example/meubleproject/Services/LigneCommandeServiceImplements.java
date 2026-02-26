package com.example.meubleproject.Services;

import com.example.meubleproject.Entities.LigneCommande;
import com.example.meubleproject.Repositories.LigneCommandeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LigneCommandeServiceImplements implements IServiceLignCommande {

    private final LigneCommandeRepo ligneCommandeRepo;

    public LigneCommandeServiceImplements(LigneCommandeRepo ligneCommandeRepo) {
        this.ligneCommandeRepo = ligneCommandeRepo;
    }

    @Override
    public LigneCommande create(LigneCommande ligneCommande) {
        return ligneCommandeRepo.save(ligneCommande);
    }

    @Override
    public LigneCommande update(Long id, LigneCommande ligneCommande) {
        LigneCommande existing = ligneCommandeRepo.findById(id).orElseThrow(() -> new RuntimeException("LigneCommande not found"));
        existing.setProduitId(ligneCommande.getProduitId());
        existing.setQuantite(ligneCommande.getQuantite());
        existing.setPrixUnitaire(ligneCommande.getPrixUnitaire());
        existing.setSousTotal(ligneCommande.getSousTotal());
        existing.setCommande(ligneCommande.getCommande());
        return ligneCommandeRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        ligneCommandeRepo.deleteById(id);
    }

    @Override
    public Optional<LigneCommande> getById(Long id) {
        return ligneCommandeRepo.findById(id);
    }

    @Override
    public List<LigneCommande> getByCommandeId(Long commandeId) {
        return ligneCommandeRepo.findByCommandeId(commandeId);
    }

    @Override
    public List<LigneCommande> getAll() {
        return ligneCommandeRepo.findAll();
    }

    @Override
    public void deleteByCommandeId(Long commandeId) {
        ligneCommandeRepo.deleteByCommandeId(commandeId);
    }
}