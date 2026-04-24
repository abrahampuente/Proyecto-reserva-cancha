package cl.duoc.userservice.service;

import cl.duoc.userservice.dto.UserRequest;
import cl.duoc.userservice.dto.UserResponse;
import cl.duoc.userservice.exception.DuplicateResourceException;
import cl.duoc.userservice.exception.ResourceNotFoundException;
import cl.duoc.userservice.model.User;
import cl.duoc.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponse create(UserRequest request) {
        log.info("Intentando crear usuario con email: {}", request.getEmail());

        if (repository.existsByEmail(request.getEmail())) {
            log.warn("No se puede crear el usuario. El correo ya existe: {}", request.getEmail());
            throw new DuplicateResourceException("Ya existe un usuario con ese correo");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole("CLIENTE");
        user.setStatus("ACTIVO");

        User savedUser = repository.save(user);

        log.info("Usuario creado correctamente con id: {}", savedUser.getId());

        return mapToResponse(savedUser);
    }

    public List<UserResponse> getAll() {
        log.info("Consultando lista de usuarios");
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getById(Long id) {
        log.info("Buscando usuario con id: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Usuario no encontrado con id: " + id);
                });

        return mapToResponse(user);
    }

    public UserResponse update(Long id, UserRequest request) {
        log.info("Intentando actualizar usuario con id: {}", id);

        User existingUser = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede actualizar. Usuario no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Usuario no encontrado con id: " + id);
                });

        if (!existingUser.getEmail().equals(request.getEmail())
                && repository.existsByEmail(request.getEmail())) {

            log.warn("No se puede actualizar el usuario. El nuevo correo ya existe: {}", request.getEmail());
            throw new DuplicateResourceException("Ya existe un usuario con ese correo");
        }

        existingUser.setFullName(request.getFullName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhone(request.getPhone());


        User updatedUser = repository.save(existingUser);

        log.info("Usuario actualizado correctamente con id: {}", updatedUser.getId());

        return mapToResponse(updatedUser);
    }

    public void delete(Long id) {
        log.info("Intentando eliminar usuario con id: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede eliminar. Usuario no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Usuario no encontrado con id: " + id);
                });

        repository.delete(user);

        log.info("Usuario eliminado correctamente con id: {}", id);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus()
        );
    }
}