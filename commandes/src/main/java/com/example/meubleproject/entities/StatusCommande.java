package com.example.meubleproject.entities;

public enum StatusCommande {
	BROUILLON,
	/** Commande persistée ; validation stock catalogue en cours via RabbitMQ */
	EN_ATTENTE_VALIDATION,
	VALIDEE,
	EN_PREPARATION,
	EXPEDIEE,
	LIVREE,
	ANNULEE
}
