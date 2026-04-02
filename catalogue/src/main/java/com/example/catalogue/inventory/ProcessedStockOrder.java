package com.example.catalogue.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "processed_stock_orders")
public class ProcessedStockOrder {

	@Id
	private Long orderId;

	@Column(nullable = false)
	private Instant traiteLe;

	public ProcessedStockOrder() {
	}

	public ProcessedStockOrder(Long orderId, Instant traiteLe) {
		this.orderId = orderId;
		this.traiteLe = traiteLe;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Instant getTraiteLe() {
		return traiteLe;
	}

	public void setTraiteLe(Instant traiteLe) {
		this.traiteLe = traiteLe;
	}
}
