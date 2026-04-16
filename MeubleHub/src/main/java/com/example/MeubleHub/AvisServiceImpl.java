package com.example.MeubleHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvisServiceImpl implements AvisService {
    @Autowired
    private AvisRepository avisRepository;



    @Override
    public Optional<Avis> findById(Long id) {
        return avisRepository.findById(id);
    }

    @Override
    public List<Avis> findAll() {
        return avisRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        avisRepository.deleteById(id);
    }
    @Override
    public Avis save(Avis avis) {
        avis.setDateAvis(LocalDateTime.now()); // ← ajoute cette ligne
        return avisRepository.save(avis);
    }
}
