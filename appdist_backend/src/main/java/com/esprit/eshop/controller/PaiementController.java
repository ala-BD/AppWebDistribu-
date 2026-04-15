package com.esprit.eshop.controller;

import com.esprit.eshop.domain.Paiement;
import com.esprit.eshop.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;

    @GetMapping
    public List<Paiement> getAllPaiements() {
        return paiementService.getAllPaiements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getPaiementById(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.getPaiementById(id));
    }

    @PostMapping
    public ResponseEntity<Paiement> createPaiement(@RequestBody Paiement paiement) {
        return ResponseEntity.ok(paiementService.createPaiement(paiement));
    }

    @PutMapping("/{id}/valider")
    public ResponseEntity<Paiement> validerPaiement(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.validerPaiement(id));
    }

    @PutMapping("/{id}/echouer")
    public ResponseEntity<Paiement> echouerPaiement(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.echouerPaiement(id));
    }
}
