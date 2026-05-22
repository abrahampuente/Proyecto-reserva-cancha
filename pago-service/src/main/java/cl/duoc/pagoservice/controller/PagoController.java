package cl.duoc.pagoservice.controller;

import cl.duoc.pagoservice.dto.PagoRequest;
import cl.duoc.pagoservice.dto.PagoResponse;
import cl.duoc.pagoservice.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoResponse> create(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> getAll() {
        return ResponseEntity.ok(pagoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.getById(id));
    }
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.existsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}