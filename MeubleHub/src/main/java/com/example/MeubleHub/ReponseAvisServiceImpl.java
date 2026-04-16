package com.example.MeubleHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReponseAvisServiceImpl implements ReponseAvisService {
    @Autowired
    private ReponseAvisRepository reponseAvisRepository;

    @Override
    public ReponseAvis save(ReponseAvis reponseAvis) {
        reponseAvis.setDateReponse(LocalDateTime.now()); // ← auto date
        return reponseAvisRepository.save(reponseAvis);
    }

    @Override
    public Optional<ReponseAvis> findById(Long id) {
        return reponseAvisRepository.findById(id);
    }

    @Override
    public List<ReponseAvis> findAll() {
        return reponseAvisRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        reponseAvisRepository.deleteById(id);
    }
}
