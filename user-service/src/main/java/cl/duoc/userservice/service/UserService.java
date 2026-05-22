package cl.duoc.userservice.service;

import cl.duoc.userservice.dto.UserRequest;
import cl.duoc.userservice.dto.UserResponse;
import cl.duoc.userservice.exception.BusinessRuleException;
import cl.duoc.userservice.exception.DuplicateResourceException;
import cl.duoc.userservice.exception.ResourceNotFoundException;
import cl.duoc.userservice.model.User;
import cl.duoc.userservice.model.UserProfile;
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
            throw new DuplicateResourceException("Ya existe un usuario con ese correo");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(validateRole(request.getRole()));
        user.setStatus("ACTIVO");

        if (request.getProfile() != null) {
            UserProfile profile = new UserProfile();
            profile.setAddress(request.getProfile().getAddress());
            profile.setCity(request.getProfile().getCity());
            profile.setCommune(request.getProfile().getCommune());
            profile.setUser(user);
            user.setProfile(profile);
        }

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
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        return mapToResponse(user);
    }

    public UserResponse update(Long id, UserRequest request) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (repository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException("Ya existe un usuario con ese correo");
        }

        existingUser.setFullName(request.getFullName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhone(request.getPhone());

        if (request.getRole() != null) {
            existingUser.setRole(validateRole(request.getRole()));
        }

        if (request.getProfile() != null) {
            UserProfile profile = existingUser.getProfile();

            if (profile == null) {
                profile = new UserProfile();
                profile.setUser(existingUser);
                existingUser.setProfile(profile);
            }

            profile.setAddress(request.getProfile().getAddress());
            profile.setCity(request.getProfile().getCity());
            profile.setCommune(request.getProfile().getCommune());
        }

        User updatedUser = repository.save(existingUser);
        return mapToResponse(updatedUser);
    }

    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        user.setStatus("INACTIVO");
        repository.save(user);
    }

    public boolean existsById(Long id) {
        return repository.existsByIdAndStatus(id, "ACTIVO");
    }

    public String getRoleById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (!"ACTIVO".equals(user.getStatus())) {
            throw new BusinessRuleException("El usuario no se encuentra activo");
        }

        return user.getRole();
    }

    private String validateRole(String role) {
        String finalRole = role != null ? role.toUpperCase() : "CLIENTE";

        if (!finalRole.equals("CLIENTE")
                && !finalRole.equals("DUENIO")
                && !finalRole.equals("ADMIN")) {
            throw new BusinessRuleException("Rol no válido. Debe ser CLIENTE, DUENIO o ADMIN");
        }

        return finalRole;
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