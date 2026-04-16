package com.example.MeubleHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReclamationServiceImpl implements ReclamationService {
    @Autowired
    private ReclamationRepository reclamationRepository;

    @Override
    public Reclamation save(Reclamation reclamation) {
        reclamation.setDateReclamation(LocalDateTime.now());
        return reclamationRepository.save(reclamation);
    }

    @Override
    public Optional<Reclamation> findById(Long id) {
        return reclamationRepository.findById(id);
    }

    @Override
    public List<Reclamation> findAll() {
        return reclamationRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        reclamationRepository.deleteById(id);
    }
}
