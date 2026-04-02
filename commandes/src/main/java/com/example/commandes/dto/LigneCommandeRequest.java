package com.example.commandes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record LigneCommandeRequest(
		@NotNull Long produitId,
		@NotNull @Positive Integer quantite,
		@NotNull @PositiveOrZero Double prixUnitaire
) {
}
