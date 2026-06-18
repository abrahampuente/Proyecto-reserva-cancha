package cl.duoc.userservice.controller;

import cl.duoc.userservice.dto.UserRequest;
import cl.duoc.userservice.dto.UserResponse;
import cl.duoc.userservice.service.UserLinkAssembler;
import cl.duoc.userservice.service.UserService;
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

import java.util.List;

@Tag(name = "Usuarios", description = "Endpoints para gestionar usuarios del sistema")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final UserLinkAssembler userLinkAssembler;

    public UserController(UserService service, UserLinkAssembler userLinkAssembler) {
        this.service = service;
        this.userLinkAssembler = userLinkAssembler;
    }

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario validando correo único y rol.")
    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese correo")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        UserResponse createdUser = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(
            summary = "Listar usuarios",
            description = "Obtiene todos los usuarios registrados. Cada usuario incluye enlaces HATEOAS en _links."
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserResponse>>> getAll() {

        List<EntityModel<UserResponse>> users = service.getAll()
                .stream()
                .map(userLinkAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<UserResponse>> collection =
                CollectionModel.of(users);

        return ResponseEntity.ok(collection);
    }

    @Operation(
            summary = "Buscar usuario por ID",
            description = "Obtiene un usuario específico por su identificador e incluye enlaces HATEOAS en _links."
    )
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> getById(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {

        UserResponse user = service.getById(id);
        return ResponseEntity.ok(userLinkAssembler.toModel(user));
    }

    @Operation(summary = "Validar existencia de usuario", description = "Retorna true si el usuario existe y está activo.")
    @ApiResponse(responseCode = "200", description = "Validación realizada correctamente")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @Operation(summary = "Obtener rol de usuario", description = "Retorna el rol de un usuario activo.")
    @ApiResponse(responseCode = "200", description = "Rol obtenido correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "400", description = "El usuario no se encuentra activo")
    @GetMapping("/{id}/role")
    public ResponseEntity<String> getRoleById(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getRoleById(id));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "Correo duplicado")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Desactivar usuario", description = "Cambia el estado del usuario a INACTIVO.")
    @ApiResponse(responseCode = "204", description = "Usuario desactivado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}