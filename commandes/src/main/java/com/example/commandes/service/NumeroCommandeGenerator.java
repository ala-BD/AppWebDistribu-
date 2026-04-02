package com.example.commandes.service;

import com.example.commandes.repository.CommandeRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NumeroCommandeGenerator {

	private final CommandeRepository commandeRepository;

	public NumeroCommandeGenerator(CommandeRepository commandeRepository) {
		this.commandeRepository = commandeRepository;
	}

	public String next() {
		String candidate;
		int attempts = 0;
		do {
			candidate = "CMD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
			attempts++;
			if (attempts > 50) {
				throw new IllegalStateException("Impossible de générer un numéro de commande unique");
			}
		} while (commandeRepository.existsByNumeroCommande(candidate));
		return candidate;
	}
}
