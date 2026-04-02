package com.example.commandes.repository;

import com.example.meubleproject.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

	boolean existsByNumeroCommande(String numeroCommande);

	@Query("select distinct c from Commande c left join fetch c.lignesCommande left join fetch c.livraison where c.numeroCommande = :n")
	Optional<Commande> findByNumeroCommandeWithDetails(@Param("n") String numeroCommande);

	@Query("select distinct c from Commande c left join fetch c.lignesCommande left join fetch c.livraison")
	java.util.List<Commande> findAllWithDetails();

	@Query("select distinct c from Commande c left join fetch c.lignesCommande left join fetch c.livraison where c.id = :id")
	Optional<Commande> findByIdWithDetails(@Param("id") Long id);
}
