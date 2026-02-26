package com.example.ApiGetway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGetwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGetwayApplication.class, args);
	}

    @Bean
    public RouteLocator getRoutes(RouteLocatorBuilder builder) {
        return builder.routes().route(
                "commande" ,
                    r->r.path("/api/commandes/**")
                            .uri("lb://meubleproject"))
               .route(
                      "ligne-commande" ,
                      r->r.path("/api/lignes-commandes/**")
                              .uri("lb://meubleproject"))


                .route(
                        "livraison" ,
                        r->r.path("/api/livraisons/**")
                                .uri("lb://meubleproject"))





                .build();
    }


}



