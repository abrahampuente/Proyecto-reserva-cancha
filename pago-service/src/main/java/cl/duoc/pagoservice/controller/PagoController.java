package cl.duoc.pagoservice.controller;

import cl.duoc.pagoservice.dto.PagoRequest;
import cl.duoc.pagoservice.dto.PagoResponse;
import cl.duoc.pagoservice.service.PagoLinkAssembler;
import cl.duoc.pagoservice.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pagos", description = "Endpoints para gestionar pagos de reservas")
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    private final PagoLinkAssembler pagoLinkAssembler;

    @Operation(summary = "Crear pago", description = "Registra el pago de una reserva existente.")
    @ApiResponse(responseCode = "201", description = "Pago creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PostMapping
    public ResponseEntity<EntityModel<PagoResponse>> create(@Valid @RequestBody PagoRequest request) {
        PagoResponse created = pagoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoLinkAssembler.toModel(created));
    }

    @Operation(summary = "Listar pagos", description = "Obtiene todos los pagos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PagoResponse>>> getAll() {
        return ResponseEntity.ok(pagoLinkAssembler.toCollectionModel(pagoService.getAll()));
    }

    @Operation(summary = "Obtener pago por ID", description = "Retorna los datos de un pago específico.")
    @ApiResponse(responseCode = "200", description = "Pago encontrado")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagoResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoLinkAssembler.toModel(pagoService.getById(id)));
    }

    @Operation(summary = "Verificar existencia de pago", description = "Usado por otros microservicios para validar que un pago existe y está activo.")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.existsById(id));
    }

    @Operation(summary = "Actualizar pago", description = "Modifica los datos de un pago existente.")
    @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PagoResponse>> update(@PathVariable Long id,
                                               @Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoLinkAssembler.toModel(pagoService.update(id, request)));
    }

    @Operation(summary = "Anular pago", description = "Marca un pago como ANULADO (soft delete).")
    @ApiResponse(responseCode = "204", description = "Pago anulado correctamente")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
