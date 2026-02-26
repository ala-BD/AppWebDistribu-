package com.example.meubleproject.Services;

import com.example.meubleproject.Entities.LigneCommande;
import java.util.List;
import java.util.Optional;

public interface IServiceLignCommande {
    LigneCommande create(LigneCommande ligneCommande);
    LigneCommande update(Long id, LigneCommande ligneCommande);
    void delete(Long id);
    Optional<LigneCommande> getById(Long id);
    List<LigneCommande> getByCommandeId(Long commandeId);
    List<LigneCommande> getAll();
    void deleteByCommandeId(Long commandeId);
}