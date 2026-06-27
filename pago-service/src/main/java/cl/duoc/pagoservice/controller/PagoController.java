package cl.duoc.pagoservice.controller;

import cl.duoc.pagoservice.dto.PagoRequest;
import cl.duoc.pagoservice.dto.PagoResponse;
import cl.duoc.pagoservice.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pagos", description = "Endpoints para gestionar pagos de reservas")
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @Operation(summary = "Crear pago", description = "Registra el pago de una reserva existente.")
    @ApiResponse(responseCode = "201", description = "Pago creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PostMapping
    public ResponseEntity<PagoResponse> create(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.create(request));
    }

    @Operation(summary = "Listar pagos", description = "Obtiene todos los pagos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<PagoResponse>> getAll() {
        return ResponseEntity.ok(pagoService.getAll());
    }

    @Operation(summary = "Obtener pago por ID", description = "Retorna los datos de un pago específico.")
