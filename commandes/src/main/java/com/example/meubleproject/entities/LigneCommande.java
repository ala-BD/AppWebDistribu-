package com.example.meubleproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "lignes_commande")
public class LigneCommande {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "commande_id", nullable = false)
	@JsonBackReference("lignes")
	private Commande commande;

	@Column(nullable = false)
	private Long produitId;

	@Column(nullable = false)
	private Integer quantite;

	@Column(nullable = false)
	private Double prixUnitaire;

	@Column(nullable = false)
	private Double sousTotal;

	public LigneCommande() {
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

	public Long getProduitId() {
		return produitId;
	}

	public void setProduitId(Long produitId) {
		this.produitId = produitId;
	}

	public Integer getQuantite() {
		return quantite;
	}

	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}

	public Double getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(Double prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	public Double getSousTotal() {
		return sousTotal;
	}

	public void setSousTotal(Double sousTotal) {
		this.sousTotal = sousTotal;
	}
}
