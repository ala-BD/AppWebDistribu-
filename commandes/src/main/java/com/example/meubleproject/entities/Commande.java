package com.example.meubleproject.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
public class Commande {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String numeroCommande;

	@Column(nullable = false)
	private Long clientId;

	@Column(nullable = false)
	private LocalDateTime dateCommande;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 50)
	private StatusCommande statut;

	@Column(nullable = false)
	private Double total;

	private String adresseLivraison;

	private LocalDate dateLivraisonPrevue;

	@Column(name = "message_validation", columnDefinition = "TEXT")
	private String messageValidation;

	@OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("lignes")
	private List<LigneCommande> lignesCommande = new ArrayList<>();

	@OneToOne(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("livraison")
	private Livraison livraison;

	public Commande() {
	}

	public Commande(Long id, String numeroCommande, Long clientId, LocalDateTime dateCommande, StatusCommande statut,
			Double total, String adresseLivraison, LocalDate dateLivraisonPrevue,
			List<LigneCommande> lignesCommande, Livraison livraison) {
		this.id = id;
		this.numeroCommande = numeroCommande;
		this.clientId = clientId;
		this.dateCommande = dateCommande;
		this.statut = statut;
		this.total = total;
		this.adresseLivraison = adresseLivraison;
		this.dateLivraisonPrevue = dateLivraisonPrevue;
		this.lignesCommande = lignesCommande != null ? lignesCommande : new ArrayList<>();
		this.livraison = livraison;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroCommande() {
		return numeroCommande;
	}

	public void setNumeroCommande(String numeroCommande) {
		this.numeroCommande = numeroCommande;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public LocalDateTime getDateCommande() {
		return dateCommande;
	}

	public void setDateCommande(LocalDateTime dateCommande) {
		this.dateCommande = dateCommande;
	}

	public StatusCommande getStatut() {
		return statut;
	}

	public void setStatut(StatusCommande statut) {
		this.statut = statut;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getAdresseLivraison() {
		return adresseLivraison;
	}

	public void setAdresseLivraison(String adresseLivraison) {
		this.adresseLivraison = adresseLivraison;
	}

	public LocalDate getDateLivraisonPrevue() {
		return dateLivraisonPrevue;
	}

	public void setDateLivraisonPrevue(LocalDate dateLivraisonPrevue) {
		this.dateLivraisonPrevue = dateLivraisonPrevue;
	}

	public String getMessageValidation() {
		return messageValidation;
	}

	public void setMessageValidation(String messageValidation) {
		this.messageValidation = messageValidation;
	}

	public List<LigneCommande> getLignesCommande() {
		return lignesCommande;
	}

	public void setLignesCommande(List<LigneCommande> lignesCommande) {
		this.lignesCommande = lignesCommande != null ? lignesCommande : new ArrayList<>();
	}

	public Livraison getLivraison() {
		return livraison;
	}

	public void setLivraison(Livraison livraison) {
		this.livraison = livraison;
	}

	public void clearLignes() {
		lignesCommande.clear();
	}

	public void addLigne(LigneCommande ligne) {
		lignesCommande.add(ligne);
		ligne.setCommande(this);
	}
}
