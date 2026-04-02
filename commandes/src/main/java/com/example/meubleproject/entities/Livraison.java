package com.example.meubleproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "livraisons")
public class Livraison {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commande_id", unique = true)
	@JsonBackReference("livraison")
	private Commande commande;

	private String transporteur;

	private String numeroSuivi;

	private LocalDateTime dateExpedition;

	@Column(columnDefinition = "TEXT")
	private String observations;

	public Livraison() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Commande getCommande() {
		return commande;
	}

	public void setCommande(Commande commande) {
		this.commande = commande;
	}

	public String getTransporteur() {
		return transporteur;
	}

	public void setTransporteur(String transporteur) {
		this.transporteur = transporteur;
	}

	public String getNumeroSuivi() {
		return numeroSuivi;
	}

	public void setNumeroSuivi(String numeroSuivi) {
		this.numeroSuivi = numeroSuivi;
	}

	public LocalDateTime getDateExpedition() {
		return dateExpedition;
	}

	public void setDateExpedition(LocalDateTime dateExpedition) {
		this.dateExpedition = dateExpedition;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}
}
