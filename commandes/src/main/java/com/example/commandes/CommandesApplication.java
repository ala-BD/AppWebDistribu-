package com.example.commandes;

import com.example.meubleproject.entities.Commande;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.example.commandes", "com.example.meubleproject"})
@EntityScan(basePackageClasses = Commande.class)
@EnableRabbit
@EnableDiscoveryClient
public class CommandesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommandesApplication.class, args);
	}
}
