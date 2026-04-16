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
                "condidat" ,
                    r->r.path("/condidat/**")
                            .uri("lb://CONDIDAT"))
               .route(
                      "jobs" ,
                      r->r.path("/jobs/**")
                              .uri("lb://JOBMS4TWIN1"))


                .route(
                        "avis" ,
                        r->r.path("/avis", "/avis/**", "/avi", "/avi/**")
                                .uri("lb://MeubleHub"))

                .route(
                        "reclamations" ,
                        r->r.path("/reclamations", "/reclamations/**", "/reclamation", "/reclamation/**")
                                .uri("lb://MeubleHub"))

                .route(
                        "produit" ,
                        r->r.path("/produit", "/produit/**")
                                .uri("lb://MeubleHub"))

                .route(
                        "categories" ,
                        r->r.path("/categorie", "/categorie/**")
                                .uri("lb://MeubleHub"))

                .route(
                        "reponseavis" ,
                        r->r.path("/reponseavis", "/reponseavis/**", "/reponses-avis", "/reponses-avis/**")
                                .uri("lb://MeubleHub"))



                .build();
    }


}



