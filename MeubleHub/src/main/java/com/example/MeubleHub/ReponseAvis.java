package com.example.MeubleHub;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ReponseAvis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long avisId;
    private Long vendeurId;
    @Column(columnDefinition = "TEXT")
    private String message;
    private LocalDateTime dateReponse;

    public ReponseAvis() {}
    public ReponseAvis(Long id, Long avisId, Long vendeurId, LocalDateTime dateReponse, String message) {
        this.id = id;
        this.avisId = avisId;
        this.vendeurId = vendeurId;
        this.dateReponse = dateReponse;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getAvisId() {
        return avisId;
    }

    public Long getVendeurId() {
        return vendeurId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateReponse() {
        return dateReponse;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAvisId(Long avisId) {
        this.avisId = avisId;
    }

    public void setVendeurId(Long vendeurId) {
        this.vendeurId = vendeurId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateReponse(LocalDateTime dateReponse) {
        this.dateReponse = dateReponse;
    }
}
