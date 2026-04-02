package com.example.commandes.dto;

import java.time.LocalDateTime;

public record LivraisonRequest(
		String transporteur,
		String numeroSuivi,
		LocalDateTime dateExpedition,
		String observations
) {
}
