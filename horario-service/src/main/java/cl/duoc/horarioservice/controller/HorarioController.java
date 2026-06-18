package cl.duoc.horarioservice.controller;

import cl.duoc.horarioservice.dto.HorarioRequest;
import cl.duoc.horarioservice.dto.HorarioResponse;
import cl.duoc.horarioservice.service.HorarioLinkAssembler;
import cl.duoc.horarioservice.service.HorarioService;
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

@Tag(name = "Horarios", description = "Endpoints para gestionar horarios de canchas")
@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    private final HorarioService service;
    private final HorarioLinkAssembler horarioLinkAssembler;

    public HorarioController(HorarioService service, HorarioLinkAssembler horarioLinkAssembler) {
        this.service = service;
        this.horarioLinkAssembler = horarioLinkAssembler;
    }

    @Operation(summary = "Crear horario", description = "Crea un nuevo horario asociado a una cancha existente.")
    @ApiResponse(responseCode = "201", description = "Horario creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PostMapping
    public ResponseEntity<HorarioResponse> create(@Valid @RequestBody HorarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(
            summary = "Listar horarios",
            description = "Obtiene todos los horarios registrados. Cada horario incluye enlaces HATEOAS en _links."
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<HorarioResponse>>> getAll() {
        List<EntityModel<HorarioResponse>> horarios = service.getAll()
                .stream()
                .map(horarioLinkAssembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        horarios,
                        linkTo(methodOn(HorarioController.class).getAll()).withSelfRel()
                )
        );
    }

    @Operation(
            summary = "Buscar horario por ID",
            description = "Obtiene un horario específico por su identificador e incluye enlaces HATEOAS en _links."
    )
    @ApiResponse(responseCode = "200", description = "Horario encontrado")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<HorarioResponse>> getById(
            @Parameter(description = "ID del horario", example = "1")
            @PathVariable Long id) {
        HorarioResponse horario = service.getById(id);
        return ResponseEntity.ok(horarioLinkAssembler.toModel(horario));
    }

    @Operation(summary = "Validar existencia de horario", description = "Retorna true si el horario existe y está activo.")
    @ApiResponse(responseCode = "200", description = "Validación realizada correctamente")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID del horario", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @Operation(summary = "Actualizar horario", description = "Actualiza los datos de un horario existente.")
    @ApiResponse(responseCode = "200", description = "Horario actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PutMapping("/{id}")
    public ResponseEntity<HorarioResponse> update(
            @Parameter(description = "ID del horario", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody HorarioRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Desactivar horario", description = "Cambia el estado del horario a INACTIVO y deja disponible en false.")
    @ApiResponse(responseCode = "204", description = "Horario desactivado correctamente")
    @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del horario", example = "1")
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}