package com.example.commandes.web;

import com.example.commandes.dto.CommandeCreateRequest;
import com.example.commandes.dto.CommandeUpdateRequest;
import com.example.commandes.service.CommandeService;
import com.example.meubleproject.entities.Commande;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commandes")
public class CommandeController {

	private final CommandeService commandeService;

	public CommandeController(CommandeService commandeService) {
		this.commandeService = commandeService;
	}

	@GetMapping
	public ResponseEntity<List<Commande>> list() {
		return ResponseEntity.ok(commandeService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Commande> getById(@PathVariable Long id) {
		return ResponseEntity.ok(commandeService.findById(id));
	}

	@GetMapping("/numero/{numero}")
	public ResponseEntity<Commande> getByNumero(@PathVariable String numero) {
		return ResponseEntity.ok(commandeService.findByNumero(numero));
	}

	@PostMapping
	public ResponseEntity<Commande> create(@Valid @RequestBody CommandeCreateRequest request) {
		Commande saved = commandeService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Commande> update(@PathVariable Long id, @Valid @RequestBody CommandeUpdateRequest request) {
		return ResponseEntity.ok(commandeService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		commandeService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
