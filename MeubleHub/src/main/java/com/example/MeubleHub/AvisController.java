package com.example.MeubleHub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avis")
public class AvisController {
    @Autowired
    private AvisService avisService;

    @PostMapping
    public ResponseEntity<Avis> create(@RequestBody Avis avis) {
        Avis saved = avisService.save(avis);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Avis> getAll() {
        return avisService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avis> getById(@PathVariable Long id) {
        return avisService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avis> update(@PathVariable Long id, @RequestBody Avis avis) {
        return avisService.findById(id)
                .map(existing -> {
                    avis.setId(id);
                    Avis updated = avisService.save(avis);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (avisService.findById(id).isPresent()) {
            avisService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
