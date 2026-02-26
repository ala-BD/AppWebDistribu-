package com.example.meubleproject.Services;

import com.example.meubleproject.Entities.Commande;
import java.util.List;
import java.util.Optional;

public interface IServiceCommande {
    Commande create(Commande commande);
    Commande update(Long id, Commande commande);
    void delete(Long id);
    Optional<Commande> getById(Long id);
    Optional<Commande> getByNumeroCommande(String numeroCommande);
    List<Commande> getAll();
}