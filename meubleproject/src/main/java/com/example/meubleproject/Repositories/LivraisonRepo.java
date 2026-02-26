package com.example.meubleproject.Repositories;

import com.example.meubleproject.Entities.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivraisonRepo extends JpaRepository<Livraison, Long> {
    Optional<Livraison> findByCommandeId(Long commandeId);
    boolean existsByCommandeId(Long commandeId);
}