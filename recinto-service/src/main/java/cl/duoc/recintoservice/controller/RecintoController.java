package cl.duoc.recintoservice.controller;

import cl.duoc.recintoservice.dto.RecintoRequest;
import cl.duoc.recintoservice.dto.RecintoResponse;
import cl.duoc.recintoservice.service.RecintoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recintos")
public class RecintoController {

    private final RecintoService service;

    public RecintoController(RecintoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RecintoResponse> create(@Valid @RequestBody RecintoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<RecintoResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecintoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecintoResponse> update(@PathVariable Long id, @Valid @RequestBody RecintoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}