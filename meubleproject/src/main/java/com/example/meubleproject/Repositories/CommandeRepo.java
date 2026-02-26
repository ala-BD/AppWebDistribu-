package com.example.meubleproject.Repositories;

import com.example.meubleproject.Entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommandeRepo extends JpaRepository<Commande, Long> {
    Optional<Commande> findByNumeroCommande(String numeroCommande);
    boolean existsByNumeroCommande(String numeroCommande);
}