package com.example.MeubleHub;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clientId;
    private Long produitId;
    private Integer note; // 1-5
    @Column(columnDefinition = "TEXT")
    private String commentaire;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    private LocalDateTime dateAvis;
    private Boolean approuve;

    public Avis() {}

    public Avis(Long id, Long clientId, Integer note, Long produitId, String commentaire, LocalDateTime dateAvis, Boolean approuve) {
        this.id = id;
        this.clientId = clientId;
        this.note = note;
        this.produitId = produitId;
        this.commentaire = commentaire;
        this.dateAvis = dateAvis;
        this.approuve = approuve;
    }

    public Long getId() {
        return id;
    }

    public Long getProduitId() {
        return produitId;
    }

    public Long getClientId() {
        return clientId;
    }

    public Integer getNote() {
        return note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public LocalDateTime getDateAvis() {
        return dateAvis;
    }

    public Boolean getApprouve() {
        return approuve;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setApprouve(Boolean approuve) {
        this.approuve = approuve;
    }

    public void setDateAvis(LocalDateTime dateAvis) {
        this.dateAvis = dateAvis;
    }
}
