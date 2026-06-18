package cl.duoc.reservaservice.controller;

import cl.duoc.reservaservice.dto.ReservaRequest;
import cl.duoc.reservaservice.dto.ReservaResponse;
import cl.duoc.reservaservice.service.ReservaLinkAssembler;
import cl.duoc.reservaservice.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Tag(name = "Reservas", description = "Endpoints para gestionar reservas de canchas deportivas")
@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaLinkAssembler reservaLinkAssembler;

    @Operation(
            summary = "Crear una reserva",
            description = "Crea una nueva reserva validando usuario, cancha, horario y disponibilidad."
    )
    @ApiResponse(responseCode = "201", description = "Reserva creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PostMapping
    public ResponseEntity<ReservaResponse> create(@Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.create(request));
    }

    @Operation(
            summary = "Listar reservas",
            description = "Obtiene todas las reservas registradas. Cada reserva incluye enlaces HATEOAS en _links."
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ReservaResponse>>> getAll() {

        List<EntityModel<ReservaResponse>> reservas = reservaService.getAll()
                .stream()
                .map(reservaLinkAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<ReservaResponse>> collection =
                CollectionModel.of(
                        reservas,
                        linkTo(methodOn(ReservaController.class).getAll()).withSelfRel()
                );

        return ResponseEntity.ok(collection);
    }

    @Operation(
            summary = "Buscar reserva por ID",
            description = "Obtiene una reserva específica por su identificador e incluye enlaces HATEOAS en _links."
    )
    @ApiResponse(responseCode = "200", description = "Reserva encontrada")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReservaResponse>> getById(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id) {

        ReservaResponse reserva = reservaService.getById(id);
        return ResponseEntity.ok(reservaLinkAssembler.toModel(reserva));
    }

    @Operation(
            summary = "Validar existencia de reserva",
            description = "Retorna true si la reserva existe y no está cancelada."
    )
    @ApiResponse(responseCode = "200", description = "Validación realizada correctamente")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(reservaService.existsById(id));
    }

    @Operation(
            summary = "Actualizar reserva",
            description = "Actualiza los datos de una reserva existente."
    )
    @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> update(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.update(id, request));
    }

    @Operation(
            summary = "Cancelar reserva",
            description = "Cancela una reserva cambiando su estado a CANCELADA."
    )
    @ApiResponse(responseCode = "204", description = "Reserva cancelada correctamente")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}