package cl.duoc.mantenimientoservice.controller;

import cl.duoc.mantenimientoservice.dto.MantenimientoRequest;
import cl.duoc.mantenimientoservice.dto.MantenimientoResponse;
import cl.duoc.mantenimientoservice.service.MantenimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mantenimientos")
@RequiredArgsConstructor
public class MantenimientoController {

    private final MantenimientoService service;

    @PostMapping
    public ResponseEntity<MantenimientoResponse> create(
            @Valid @RequestBody MantenimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<MantenimientoResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MantenimientoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MantenimientoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}