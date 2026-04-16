package com.example.MeubleHub;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity

public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clientId;
    private Long commandeId;
    @Enumerated(EnumType.STRING)
    private TypeReclamation typeReclamation;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime dateReclamation;
    @Enumerated(EnumType.STRING)
    private StatutReclamation statut;
    private LocalDate dateResolution;

    public Reclamation() {}

    public Reclamation(Long id, Long clientId, Long commandeId, TypeReclamation typeReclamation, String description, LocalDateTime dateReclamation, StatutReclamation statut, LocalDate dateResolution) {
        this.id = id;
        this.clientId = clientId;
        this.commandeId = commandeId;
        this.typeReclamation = typeReclamation;
        this.description = description;
        this.dateReclamation = dateReclamation;
        this.statut = statut;
        this.dateResolution = dateResolution;


    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public TypeReclamation getTypeReclamation() {
        return typeReclamation;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateReclamation() {
        return dateReclamation;
    }

    public StatutReclamation getStatut() {
        return statut;
    }

    public LocalDate getDateResolution() {
        return dateResolution;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
    }

    public void setTypeReclamation(TypeReclamation typeReclamation) {
        this.typeReclamation = typeReclamation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateReclamation(LocalDateTime dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public void setStatut(StatutReclamation statut) {
        this.statut = statut;
    }

    public void setDateResolution(LocalDate dateResolution) {
        this.dateResolution = dateResolution;
    }


}
