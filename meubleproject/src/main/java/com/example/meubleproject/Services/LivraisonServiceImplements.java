package com.example.meubleproject.Services;

import com.example.meubleproject.Entities.Livraison;
import com.example.meubleproject.Repositories.LivraisonRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivraisonServiceImplements implements IServiceLivraison {

    private final LivraisonRepo livraisonRepo;

    public LivraisonServiceImplements(LivraisonRepo livraisonRepo) {
        this.livraisonRepo = livraisonRepo;
    }

    @Override
    public Livraison create(Livraison livraison) {
        return livraisonRepo.save(livraison);
    }

    @Override
    public Livraison update(Long id, Livraison livraison) {
        Livraison existing = livraisonRepo.findById(id).orElseThrow(() -> new RuntimeException("Livraison not found"));
        existing.setLivreurId(livraison.getLivreurId());
        existing.setDateLivraison(livraison.getDateLivraison());
        existing.setStatut(livraison.getStatut());
        existing.setSignatureClient(livraison.getSignatureClient());
        existing.setCommande(livraison.getCommande());
        return livraisonRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        livraisonRepo.deleteById(id);
    }

    @Override
    public Optional<Livraison> getById(Long id) {
        return livraisonRepo.findById(id);
    }

    @Override
    public Optional<Livraison> getByCommandeId(Long commandeId) {
        return livraisonRepo.findByCommandeId(commandeId);
    }

    @Override
    public List<Livraison> getAll() {
        return livraisonRepo.findAll();
    }
}