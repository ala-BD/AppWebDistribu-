package com.example.MeubleHub;

import java.util.List;
import java.util.Optional;

public interface ReponseAvisService {
    ReponseAvis save(ReponseAvis reponseAvis);
    Optional<ReponseAvis> findById(Long id);
    List<ReponseAvis> findAll();
    void deleteById(Long id);
}
