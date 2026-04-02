package com.example.apigateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Downstream bases for catalogue and commandes. Eureka registers names in uppercase
 * (e.g. CATALOGUE); use {@code lb://CATALOGUE}. If the registry is empty or slow to sync,
 * set {@code apigateway.catalogue-uri=http://127.0.0.1:8095} (same for commandes / 8098).
 */
@ConfigurationProperties(prefix = "apigateway")
public class GatewayRoutingProperties {

	/**
	 * Default matches typical Eureka application name for {@code spring.application.name=catalogue}.
	 */
	private String catalogueUri = "lb://CATALOGUE";

	private String commandesUri = "lb://COMMANDES";

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
}
