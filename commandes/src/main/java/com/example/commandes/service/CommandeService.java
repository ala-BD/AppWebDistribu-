package com.example.commandes.service;

import com.example.commandes.dto.CommandeCreateRequest;
import com.example.commandes.dto.CommandeUpdateRequest;
import com.example.commandes.dto.LigneCommandeRequest;
import com.example.commandes.dto.LivraisonRequest;
import com.example.commandes.repository.CommandeRepository;
import com.example.meubleproject.entities.Commande;
import com.example.meubleproject.entities.LigneCommande;
import com.example.meubleproject.entities.Livraison;
import com.example.meubleproject.entities.StatusCommande;
import com.example.commandes.messaging.OrderStockValidationPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommandeService {

	private final CommandeRepository commandeRepository;
	private final NumeroCommandeGenerator numeroCommandeGenerator;
	private final OrderStockValidationPublisher stockValidationPublisher;

	public CommandeService(CommandeRepository commandeRepository, NumeroCommandeGenerator numeroCommandeGenerator,
			OrderStockValidationPublisher stockValidationPublisher) {
		this.commandeRepository = commandeRepository;
		this.numeroCommandeGenerator = numeroCommandeGenerator;
		this.stockValidationPublisher = stockValidationPublisher;
	}

	@Transactional(readOnly = true)
	public List<Commande> findAll() {
		return commandeRepository.findAllWithDetails();
	}

	@Transactional(readOnly = true)
	public Commande findById(Long id) {
		return commandeRepository.findByIdWithDetails(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande introuvable"));
	}

	@Transactional(readOnly = true)
	public Commande findByNumero(String numero) {
		return commandeRepository.findByNumeroCommandeWithDetails(numero)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande introuvable"));
	}

	@Transactional
	public Commande create(CommandeCreateRequest req) {
		Commande c = new Commande();
		c.setNumeroCommande(numeroCommandeGenerator.next());
		c.setClientId(req.clientId());
		c.setDateCommande(LocalDateTime.now());
		StatusCommande demande = req.statut() != null ? req.statut() : StatusCommande.BROUILLON;
		validateStatutForNew(demande);

		c.setStatut(StatusCommande.EN_ATTENTE_VALIDATION);
		c.setMessageValidation("En attente de validation du stock (catalogue)");
		c.setAdresseLivraison(req.adresseLivraison());
		c.setDateLivraisonPrevue(req.dateLivraisonPrevue());

		attachLignes(c, req.lignes());
		c.setTotal(computeTotal(c));

		if (req.livraison() != null) {
			Livraison lv = mapLivraison(req.livraison());
			c.setLivraison(lv);
			lv.setCommande(c);
		}

		Long savedId = commandeRepository.save(c).getId();
		Commande avecDetails = commandeRepository.findByIdWithDetails(savedId)
				.orElseThrow(() -> new IllegalStateException("Commande non retrouvée après enregistrement"));

		Commande pourMessage = avecDetails;
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					stockValidationPublisher.publishValidationRequest(pourMessage);
				}
			});
		} else {
			stockValidationPublisher.publishValidationRequest(pourMessage);
		}

		return avecDetails;
	}

	@Transactional
	public Commande update(Long id, CommandeUpdateRequest req) {
		Commande c = commandeRepository.findByIdWithDetails(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande introuvable"));

		if (c.getStatut() == StatusCommande.LIVREE || c.getStatut() == StatusCommande.ANNULEE) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Impossible de modifier une commande livrée ou annulée");
		}
		if (c.getStatut() == StatusCommande.EN_ATTENTE_VALIDATION) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Modification impossible pendant la validation asynchrone du stock");
		}

		c.setClientId(req.clientId());
		c.setStatut(req.statut());
		c.setAdresseLivraison(req.adresseLivraison());
		c.setDateLivraisonPrevue(req.dateLivraisonPrevue());

		c.getLignesCommande().clear();
		attachLignes(c, req.lignes());
		c.setTotal(computeTotal(c));

		if (req.livraison() != null) {
			Livraison lv = c.getLivraison();
			if (lv == null) {
				lv = new Livraison();
				c.setLivraison(lv);
				lv.setCommande(c);
			}
			applyLivraison(lv, req.livraison());
		} else {
			c.setLivraison(null);
		}

		Long savedId = commandeRepository.save(c).getId();
		return commandeRepository.findByIdWithDetails(savedId)
				.orElseThrow(() -> new IllegalStateException("Commande non retrouvée après enregistrement"));
	}

	@Transactional
	public void delete(Long id) {
		Commande c = commandeRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Commande introuvable"));
		if (c.getStatut() != StatusCommande.BROUILLON && c.getStatut() != StatusCommande.ANNULEE
				&& c.getStatut() != StatusCommande.EN_ATTENTE_VALIDATION) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Suppression réservée aux commandes en brouillon, en attente de validation ou annulées");
		}
		commandeRepository.delete(c);
	}

	private void validateStatutForNew(StatusCommande s) {
		if (s == StatusCommande.LIVREE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Une nouvelle commande ne peut pas être directement livrée");
		}
		if (s == StatusCommande.ANNULEE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Une nouvelle commande ne peut pas être créée annulée");
		}
	}

	private void attachLignes(Commande c, List<LigneCommandeRequest> lignes) {
		for (LigneCommandeRequest r : lignes) {
			LigneCommande l = new LigneCommande();
			l.setProduitId(r.produitId());
			l.setQuantite(r.quantite());
			l.setPrixUnitaire(r.prixUnitaire());
			double sous = r.quantite() * r.prixUnitaire();
			l.setSousTotal(roundMoney(sous));
			c.addLigne(l);
		}
	}

	private Livraison mapLivraison(LivraisonRequest r) {
		Livraison lv = new Livraison();
		applyLivraison(lv, r);
		return lv;
	}

	private void applyLivraison(Livraison lv, LivraisonRequest r) {
		lv.setTransporteur(r.transporteur());
		lv.setNumeroSuivi(r.numeroSuivi());
		lv.setDateExpedition(r.dateExpedition());
		lv.setObservations(r.observations());
	}

	private double computeTotal(Commande c) {
		return roundMoney(c.getLignesCommande().stream()
				.mapToDouble(LigneCommande::getSousTotal)
				.sum());
	}

	private static double roundMoney(double v) {
		return Math.round(v * 100.0) / 100.0;
	}
}
