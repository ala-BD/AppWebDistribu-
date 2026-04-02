package com.example.catalogue.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedStockOrderRepository extends JpaRepository<ProcessedStockOrder, Long> {
}
