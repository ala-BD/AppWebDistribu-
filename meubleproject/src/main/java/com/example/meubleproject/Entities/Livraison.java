package com.example.meubleproject.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "livraisons")
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long livreurId;

    private LocalDateTime dateLivraison;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLivraison statut;

    @Lob
    private String signatureClient;

    @OneToOne
    @JoinColumn(name = "commande_id", nullable = false, unique = true)
    private Commande commande;

    public Livraison() {
    }

    public Livraison(Long id, Long livreurId, LocalDateTime dateLivraison, StatusLivraison statut, String signatureClient, Commande commande) {
        this.id = id;
        this.livreurId = livreurId;
        this.dateLivraison = dateLivraison;
        this.statut = statut;
        this.signatureClient = signatureClient;
        this.commande = commande;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLivreurId() {
        return livreurId;
    }

    public void setLivreurId(Long livreurId) {
        this.livreurId = livreurId;
    }

    public LocalDateTime getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDateTime dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public StatusLivraison getStatut() {
        return statut;
    }

    public void setStatut(StatusLivraison statut) {
        this.statut = statut;
    }

    public String getSignatureClient() {
        return signatureClient;
    }

    public void setSignatureClient(String signatureClient) {
        this.signatureClient = signatureClient;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
}