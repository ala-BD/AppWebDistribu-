package com.example.catalogue.inventory;

import com.example.catalogue.messaging.dto.OrderLineStockDto;
import com.example.catalogue.messaging.dto.OrderStockValidationRequest;
import com.example.catalogue.messaging.dto.OrderStockValidationResult;
import com.example.catalogue.produits.Produit;
import com.example.catalogue.produits.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockValidationService {

	private final ProduitRepository produitRepository;
	private final ProcessedStockOrderRepository processedStockOrderRepository;

	public StockValidationService(ProduitRepository produitRepository,
			ProcessedStockOrderRepository processedStockOrderRepository) {
		this.produitRepository = produitRepository;
		this.processedStockOrderRepository = processedStockOrderRepository;
	}

	@Transactional
	public OrderStockValidationResult validateAndApply(OrderStockValidationRequest request) {
		if (processedStockOrderRepository.existsById(request.orderId())) {
			return new OrderStockValidationResult(
					request.orderId(),
					true,
					"Commande déjà traitée (message idempotent)",
					null);
		}

		List<String> erreurs = new ArrayList<>();
		for (OrderLineStockDto ligne : request.lignes()) {
			Produit p = produitRepository.findById(ligne.produitId()).orElse(null);
			if (p == null) {
				erreurs.add("Produit introuvable: id=" + ligne.produitId());
				continue;
			}
			if (p.getQuantiteStock() == null || p.getQuantiteStock() < ligne.quantite()) {
				erreurs.add("Stock insuffisant pour produit id=" + ligne.produitId()
						+ " (demandé: " + ligne.quantite() + ", disponible: " + p.getQuantiteStock() + ")");
			}
		}

		if (!erreurs.isEmpty()) {
			return new OrderStockValidationResult(
					request.orderId(),
					false,
					"Validation stock échouée",
					erreurs);
		}

		for (OrderLineStockDto ligne : request.lignes()) {
			Produit p = produitRepository.findById(ligne.produitId()).orElseThrow();
			p.setQuantiteStock(p.getQuantiteStock() - ligne.quantite());
			produitRepository.save(p);
		}

		processedStockOrderRepository.save(new ProcessedStockOrder(request.orderId(), Instant.now()));

		return new OrderStockValidationResult(
				request.orderId(),
				true,
				"Stock déduit avec succès",
				null);
	}
}
