package com.example.apigateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Downstream bases for catalogue, commandes and appdist backend. Eureka
 * registers names in uppercase
 * (e.g. CATALOGUE); use {@code lb://CATALOGUE}. If the registry is empty or
 * slow to sync,
 * set {@code apigateway.catalogue-uri=http://127.0.0.1:8095} (same for
 * commandes / 8098).
 */
@ConfigurationProperties(prefix = "apigateway")
public class GatewayRoutingProperties {

	/**
	 * Default matches typical Eureka application name for
	 * {@code spring.application.name=catalogue}.
	 */
	private String catalogueUri = "lb://CATALOGUE";

	private String commandesUri = "lb://COMMANDES";
	private String appdistBackendUri = "http://127.0.0.1:8081";
	private String promotionsUri = "lb://PROMOTIONS";

	public String getCatalogueUri() {
		return catalogueUri;
	}

	public void setCatalogueUri(String catalogueUri) {
		this.catalogueUri = catalogueUri;
	}

	public String getCommandesUri() {
		return commandesUri;
	}

	public void setCommandesUri(String commandesUri) {
		this.commandesUri = commandesUri;
	}

	public String getAppdistBackendUri() {
		return appdistBackendUri;
	}

	public void setAppdistBackendUri(String appdistBackendUri) {
		this.appdistBackendUri = appdistBackendUri;
	}

	public String getPromotionsUri() {
		return promotionsUri;
	}

	public void setPromotionsUri(String promotionsUri) {
		this.promotionsUri = promotionsUri;
	}
}
