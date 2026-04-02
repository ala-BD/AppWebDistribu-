package com.example.commandes.messaging;

import com.example.commandes.messaging.dto.OrderLineStockDto;
import com.example.commandes.messaging.dto.OrderStockValidationRequest;
import com.example.meubleproject.entities.Commande;
import com.example.meubleproject.entities.LigneCommande;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderStockValidationPublisher {

	private final RabbitTemplate rabbitTemplate;

	public OrderStockValidationPublisher(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void publishValidationRequest(Commande commande) {
		List<OrderLineStockDto> lignes = commande.getLignesCommande().stream()
				.map(l -> new OrderLineStockDto(l.getProduitId(), l.getQuantite()))
				.toList();
		OrderStockValidationRequest payload = new OrderStockValidationRequest(
				commande.getId(),
				commande.getNumeroCommande(),
				lignes);
		rabbitTemplate.convertAndSend(
				MeubleMessagingConstants.EXCHANGE,
				MeubleMessagingConstants.RK_ORDER_STOCK_VALIDATE,
				payload);
	}
}
