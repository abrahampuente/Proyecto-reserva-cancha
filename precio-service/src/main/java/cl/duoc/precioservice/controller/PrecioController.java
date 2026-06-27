package cl.duoc.precioservice.controller;

import cl.duoc.precioservice.dto.PrecioRequest;
import cl.duoc.precioservice.dto.PrecioResponse;
import cl.duoc.precioservice.service.PrecioLinkAssembler;
import cl.duoc.precioservice.service.PrecioService;
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

@Tag(name = "Precios", description = "Endpoints para gestionar precios de canchas")
@RestController
@RequestMapping("/api/precios")
@RequiredArgsConstructor
public class PrecioController {

    private final PrecioService precioService;
    private final PrecioLinkAssembler precioLinkAssembler;

    @Operation(summary = "Crear precio", description = "Registra un nuevo precio para una cancha existente.")
    @ApiResponse(responseCode = "201", description = "Precio creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PostMapping
    public ResponseEntity<EntityModel<PrecioResponse>> create(@Valid @RequestBody PrecioRequest request) {
        PrecioResponse created = precioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(precioLinkAssembler.toModel(created));
    }

    @Operation(summary = "Listar precios", description = "Obtiene todos los precios registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de precios obtenida correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PrecioResponse>>> getAll() {
        return ResponseEntity.ok(precioLinkAssembler.toCollectionModel(precioService.getAll()));
    }

    @Operation(summary = "Obtener precio por ID", description = "Retorna los datos de un precio específico.")
    @ApiResponse(responseCode = "200", description = "Precio encontrado")
    @ApiResponse(responseCode = "404", description = "Precio no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PrecioResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(precioLinkAssembler.toModel(precioService.getById(id)));
    }

    @Operation(summary = "Verificar existencia de precio", description = "Usado por otros microservicios para validar que un precio existe y está activo.")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(precioService.existsById(id));
    }

    @Operation(summary = "Actualizar precio", description = "Modifica los datos de un precio existente.")
    @ApiResponse(responseCode = "200", description = "Precio actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "404", description = "Precio no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PrecioResponse>> update(@PathVariable Long id,
                                                 @Valid @RequestBody PrecioRequest request) {
        return ResponseEntity.ok(precioLinkAssembler.toModel(precioService.update(id, request)));
    }

    @Operation(summary = "Desactivar precio", description = "Marca un precio como INACTIVO (soft delete).")
    @ApiResponse(responseCode = "204", description = "Precio desactivado correctamente")
    @ApiResponse(responseCode = "404", description = "Precio no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        precioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
