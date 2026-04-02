package com.example.commandes.dto;

import com.example.meubleproject.entities.StatusCommande;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CommandeCreateRequest(
		@NotNull Long clientId,
		StatusCommande statut,
		String adresseLivraison,
		LocalDate dateLivraisonPrevue,
		@NotEmpty @Valid List<LigneCommandeRequest> lignes,
		@Valid LivraisonRequest livraison
) {
}
