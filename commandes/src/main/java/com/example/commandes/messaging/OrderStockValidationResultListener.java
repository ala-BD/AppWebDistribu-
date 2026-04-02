package com.example.commandes.messaging;

import com.example.commandes.messaging.dto.OrderStockValidationResult;
import com.example.commandes.repository.CommandeRepository;
import com.example.meubleproject.entities.Commande;
import com.example.meubleproject.entities.StatusCommande;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Component
public class OrderStockValidationResultListener {

	private static final Logger log = LoggerFactory.getLogger(OrderStockValidationResultListener.class);

	private final CommandeRepository commandeRepository;

	public OrderStockValidationResultListener(CommandeRepository commandeRepository) {
		this.commandeRepository = commandeRepository;
	}

	@RabbitListener(queues = MeubleMessagingConstants.QUEUE_COMMANDES_STOCK_RESULT)
	@Transactional
	public void onStockValidationResult(OrderStockValidationResult result) {
		Commande c = commandeRepository.findById(result.orderId()).orElse(null);
		if (c == null) {
			log.warn("Résultat stock reçu pour commande inexistante id={}", result.orderId());
			return;
		}
		if (c.getStatut() != StatusCommande.EN_ATTENTE_VALIDATION) {
			log.info("Ignorer résultat stock pour commande {} statut={}", result.orderId(), c.getStatut());
			return;
		}
		if (result.success()) {
			c.setStatut(StatusCommande.VALIDEE);
			c.setMessageValidation(result.message() != null ? result.message() : "Stock validé et réservé");
		} else {
			c.setStatut(StatusCommande.ANNULEE);
			String detail = result.erreurs() != null && !result.erreurs().isEmpty()
					? result.erreurs().stream().collect(Collectors.joining("; "))
					: result.message();
			c.setMessageValidation(detail != null ? detail : "Validation stock refusée");
		}
		commandeRepository.save(c);
	}
}
