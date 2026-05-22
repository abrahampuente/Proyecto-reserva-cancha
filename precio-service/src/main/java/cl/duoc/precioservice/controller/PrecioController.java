package cl.duoc.precioservice.controller;

import cl.duoc.precioservice.dto.PrecioRequest;
import cl.duoc.precioservice.dto.PrecioResponse;
import cl.duoc.precioservice.service.PrecioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/precios")
@RequiredArgsConstructor
public class PrecioController {

    private final PrecioService precioService;

    @PostMapping
    public ResponseEntity<PrecioResponse> create(@Valid @RequestBody PrecioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(precioService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PrecioResponse>> getAll() {
        return ResponseEntity.ok(precioService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(precioService.getById(id));
    }
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(precioService.existsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrecioResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody PrecioRequest request) {
        return ResponseEntity.ok(precioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        precioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}