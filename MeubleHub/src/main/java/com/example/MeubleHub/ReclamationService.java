package com.example.MeubleHub;

import java.util.List;
import java.util.Optional;

public interface ReclamationService {
    Reclamation save(Reclamation reclamation);
    Optional<Reclamation> findById(Long id);
    List<Reclamation> findAll();
    void deleteById(Long id);
}
