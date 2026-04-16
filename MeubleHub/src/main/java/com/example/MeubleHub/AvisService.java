package com.example.MeubleHub;

import java.util.List;
import java.util.Optional;

public interface AvisService {
    Avis save(Avis avis);
    Optional<Avis> findById(Long id);
    List<Avis> findAll();
    void deleteById(Long id);
}
