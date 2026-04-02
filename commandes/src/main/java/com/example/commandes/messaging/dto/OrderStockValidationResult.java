package com.example.commandes.messaging.dto;

import java.util.List;

public record OrderStockValidationResult(
		Long orderId,
		boolean success,
		String message,
		List<String> erreurs
) {
}
