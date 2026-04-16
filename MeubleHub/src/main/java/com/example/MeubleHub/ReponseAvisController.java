package com.example.MeubleHub;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reponses-avis")
public class ReponseAvisController {
    @Autowired
    private ReponseAvisService reponseAvisService;

    @PostMapping
    public ResponseEntity<ReponseAvis> create(@RequestBody ReponseAvis reponseAvis) {
        ReponseAvis saved = reponseAvisService.save(reponseAvis);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ReponseAvis> getAll() {
        return reponseAvisService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReponseAvis> getById(@PathVariable Long id) {
        return reponseAvisService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReponseAvis> update(@PathVariable Long id, @RequestBody ReponseAvis reponseAvis) {
        return reponseAvisService.findById(id)
                .map(existing -> {
                    reponseAvis.setId(id);
                    ReponseAvis updated = reponseAvisService.save(reponseAvis);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (reponseAvisService.findById(id).isPresent()) {
            reponseAvisService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
