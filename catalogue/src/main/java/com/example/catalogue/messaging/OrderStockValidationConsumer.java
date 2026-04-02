package com.example.catalogue.messaging;

import com.example.catalogue.inventory.StockValidationService;
import com.example.catalogue.messaging.dto.OrderStockValidationRequest;
import com.example.catalogue.messaging.dto.OrderStockValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderStockValidationConsumer {

	private static final Logger log = LoggerFactory.getLogger(OrderStockValidationConsumer.class);

	private final StockValidationService stockValidationService;
	private final RabbitTemplate rabbitTemplate;

	public OrderStockValidationConsumer(StockValidationService stockValidationService, RabbitTemplate rabbitTemplate) {
		this.stockValidationService = stockValidationService;
		this.rabbitTemplate = rabbitTemplate;
	}

	@RabbitListener(queues = MeubleMessagingConstants.QUEUE_CATALOGUE_STOCK_VALIDATE)
	public void onOrderStockValidation(OrderStockValidationRequest request) {
		try {
			OrderStockValidationResult result = stockValidationService.validateAndApply(request);
			rabbitTemplate.convertAndSend(
					MeubleMessagingConstants.EXCHANGE,
					MeubleMessagingConstants.RK_ORDER_STOCK_RESULT,
					result);
		} catch (Exception e) {
			log.error("Erreur traitement validation stock commande {}", request.orderId(), e);
			rabbitTemplate.convertAndSend(
					MeubleMessagingConstants.EXCHANGE,
					MeubleMessagingConstants.RK_ORDER_STOCK_RESULT,
					new OrderStockValidationResult(
							request.orderId(),
							false,
							"Erreur interne catalogue",
							java.util.List.of(e.getMessage() != null ? e.getMessage() : "erreur")));
		}
	}
}
