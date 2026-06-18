package cl.duoc.canchaservice.controller;

import cl.duoc.canchaservice.dto.CanchaRequest;
import cl.duoc.canchaservice.dto.CanchaResponse;
import cl.duoc.canchaservice.service.CanchaLinkAssembler;
import cl.duoc.canchaservice.service.CanchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Tag(name = "Canchas", description = "Endpoints para gestionar canchas deportivas")
@RestController
@RequestMapping("/api/canchas")
public class CanchaController {

    private final CanchaService service;
    private final CanchaLinkAssembler canchaLinkAssembler;

    public CanchaController(CanchaService service, CanchaLinkAssembler canchaLinkAssembler) {
        this.service = service;
        this.canchaLinkAssembler = canchaLinkAssembler;
    }

    @Operation(summary = "Crear cancha", description = "Crea una nueva cancha asociada a un recinto existente.")
    @ApiResponse(responseCode = "201", description = "Cancha creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PostMapping
    public ResponseEntity<CanchaResponse> create(@Valid @RequestBody CanchaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(
            summary = "Listar canchas",
            description = "Obtiene todas las canchas registradas. Cada cancha incluye enlaces HATEOAS en _links."
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CanchaResponse>>> getAll() {
        List<EntityModel<CanchaResponse>> canchas = service.getAll()
                .stream()
                .map(canchaLinkAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<CanchaResponse>> collection =
                CollectionModel.of(
                        canchas,
                        linkTo(methodOn(CanchaController.class).getAll()).withSelfRel()
                );
        return ResponseEntity.ok(collection);
    }

    @Operation(
            summary = "Buscar cancha por ID",
            description = "Obtiene una cancha específica por su identificador e incluye enlaces HATEOAS en _links."
    )
    @ApiResponse(responseCode = "200", description = "Cancha encontrada")
    @ApiResponse(responseCode = "404", description = "Cancha no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CanchaResponse>> getById(
            @Parameter(description = "ID de la cancha", example = "1")
            @PathVariable Long id) {

        CanchaResponse cancha = service.getById(id);
        return ResponseEntity.ok(canchaLinkAssembler.toModel(cancha));
    }

    @Operation(summary = "Validar existencia de cancha", description = "Retorna true si la cancha existe y está activa.")
    @ApiResponse(responseCode = "200", description = "Validación realizada correctamente")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID de la cancha", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @Operation(summary = "Actualizar cancha", description = "Actualiza los datos de una cancha existente.")
    @ApiResponse(responseCode = "200", description = "Cancha actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "404", description = "Cancha no encontrada")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PutMapping("/{id}")
    public ResponseEntity<CanchaResponse> update(
            @Parameter(description = "ID de la cancha", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CanchaRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Desactivar cancha", description = "Cambia el estado de la cancha a INACTIVA.")
    @ApiResponse(responseCode = "204", description = "Cancha desactivada correctamente")
    @ApiResponse(responseCode = "404", description = "Cancha no encontrada")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la cancha", example = "1")
            @PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}