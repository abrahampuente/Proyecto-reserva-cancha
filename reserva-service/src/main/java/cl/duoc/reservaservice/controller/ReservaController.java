package cl.duoc.reservaservice.controller;
// Controller REST para gestión de reservas, expone endpoints CRUD

import cl.duoc.reservaservice.dto.ReservaRequest;
import cl.duoc.reservaservice.dto.ReservaResponse;
import cl.duoc.reservaservice.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponse> create(@Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> getAll() {
        return ResponseEntity.ok(reservaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
