package cl.duoc.canchaservice.controller;

import cl.duoc.canchaservice.dto.CanchaRequest;
import cl.duoc.canchaservice.dto.CanchaResponse;
import cl.duoc.canchaservice.service.CanchaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canchas")
public class CanchaController {

    private final CanchaService service;

    public CanchaController(CanchaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CanchaResponse> create(@Valid @RequestBody CanchaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CanchaResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanchaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CanchaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CanchaRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}