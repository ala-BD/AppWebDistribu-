package com.example.MeubleHub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReponseAvisRepository extends JpaRepository<ReponseAvis, Long> {
}
