package com.example.meubleproject.Services;

import com.example.meubleproject.Entities.Livraison;
import java.util.List;
import java.util.Optional;

public interface IServiceLivraison {
    Livraison create(Livraison livraison);
    Livraison update(Long id, Livraison livraison);
    void delete(Long id);
    Optional<Livraison> getById(Long id);
    Optional<Livraison> getByCommandeId(Long commandeId);
    List<Livraison> getAll();
}