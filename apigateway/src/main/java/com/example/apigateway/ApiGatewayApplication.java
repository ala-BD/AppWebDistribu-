package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(GatewayRoutingProperties.class)
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	/**
	 * Dev-friendly CORS so browsers / SPA can POST JSON through the gateway.
	 */
	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(List.of("*"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsWebFilter(source);
	}

	@Bean
	public RouteLocator getRoutes(RouteLocatorBuilder builder, GatewayRoutingProperties routing) {
		String catalogue = routing.getCatalogueUri();
		String commandes = routing.getCommandesUri();
		String appdistBackend = routing.getAppdistBackendUri();
		String promotions = routing.getPromotionsUri();

		return builder.routes()
				.route("condidat",
						r -> r.path("/condidat/**")
								.uri("lb://CONDIDAT"))
				.route("jobs",
						r -> r.path("/jobs/**")
								.uri("lb://JOBMS4TWIN1"))
				// Catalogue: Eureka app name is usually uppercase (CATALOGUE)
				.route("produit-root",
						r -> r.path("/produit")
								.uri(catalogue))
				.route("produit",
						r -> r.path("/produit/**")
								.uri(catalogue))
				.route("produits-alias-root",
						r -> r.path("/produits")
								.filters(f -> f.rewritePath("/produits", "/produit"))
								.uri(catalogue))
				.route("produits-alias",
						r -> r.path("/produits/**")
								.filters(f -> f.rewritePath("/produits(?<segment>.*)", "/produit${segment}"))
								.uri(catalogue))
				.route("categorie-root",
						r -> r.path("/categorie")
								.uri(catalogue))
				.route("categorie",
						r -> r.path("/categorie/**")
								.uri(catalogue))
				.route("categories-alias-root",
						r -> r.path("/categories")
								.filters(f -> f.rewritePath("/categories", "/categorie"))
								.uri(catalogue))
				.route("categories-alias",
						r -> r.path("/categories/**")
								.filters(f -> f.rewritePath("/categories(?<segment>.*)", "/categorie${segment}"))
								.uri(catalogue))
				.route("commandes-root",
						r -> r.path("/commandes")
								.uri(commandes))
				.route("commandes",
						r -> r.path("/commandes/**")
								.uri(commandes))
				.route("commande-alias-root",
						r -> r.path("/commande")
								.filters(f -> f.rewritePath("/commande", "/commandes"))
								.uri(commandes))
				.route("commande-alias",
						r -> r.path("/commande/**")
								.filters(f -> f.rewritePath("/commande(?<segment>.*)", "/commandes${segment}"))
								.uri(commandes))

				.route("paiements",
						r -> r.path("/api/paiements/**")
								.uri(appdistBackend))
				.route("users",
						r -> r.path("/api/users/**")
								.uri(appdistBackend))
				.route("promotions",
						r -> r.path("/promotions/**")
								.uri(promotions))
				.build();
	}
}
