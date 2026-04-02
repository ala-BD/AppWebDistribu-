package com.example.commandes.messaging.dto;

import java.util.List;

public record OrderStockValidationRequest(Long orderId, String numeroCommande, List<OrderLineStockDto> lignes) {
}
