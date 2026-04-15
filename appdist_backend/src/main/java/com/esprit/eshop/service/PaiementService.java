package com.esprit.eshop.service;

import com.esprit.eshop.domain.Paiement;
import com.esprit.eshop.domain.StatutPaiement;
import com.esprit.eshop.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementService {
    
    private final PaiementRepository paiementRepository;
    
    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }
    
    public Paiement getPaiementById(Long id) {
        return paiementRepository.findById(id).orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
    }
    
    public Paiement createPaiement(Paiement paiement) {
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setStatut(StatutPaiement.EN_ATTENTE);
        return paiementRepository.save(paiement);
    }
    
    public Paiement validerPaiement(Long id) {
        Paiement paiement = getPaiementById(id);
        paiement.setStatut(StatutPaiement.VALIDE);
        return paiementRepository.save(paiement);
    }
    
    public Paiement echouerPaiement(Long id) {
        Paiement paiement = getPaiementById(id);
        paiement.setStatut(StatutPaiement.ECHEC);
        return paiementRepository.save(paiement);
    }
}
