package cl.duoc.userservice.service;

import cl.duoc.userservice.dto.UserRequest;
import cl.duoc.userservice.dto.UserResponse;
import cl.duoc.userservice.exception.BusinessRuleException;
import cl.duoc.userservice.exception.DuplicateResourceException;
import cl.duoc.userservice.exception.ResourceNotFoundException;
import cl.duoc.userservice.model.User;
import cl.duoc.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    void debeCrearUsuarioCorrectamente() {

        UserRequest request = new UserRequest();
        request.setFullName("Juan Perez");
        request.setEmail("juan@test.com");
        request.setPhone("999999999");
        request.setRole("CLIENTE");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullName(request.getFullName());
        savedUser.setEmail(request.getEmail());
        savedUser.setPhone(request.getPhone());
        savedUser.setRole("CLIENTE");
        savedUser.setStatus("ACTIVO");

        when(repository.existsByEmail("juan@test.com")).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(savedUser);


        UserResponse response = userService.create(request);


        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan Perez", response.getFullName());
        assertEquals("CLIENTE", response.getRole());
        assertEquals("ACTIVO", response.getStatus());

        verify(repository, times(1)).existsByEmail("juan@test.com");
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void noDebeCrearUsuarioConCorreoDuplicado() {

        UserRequest request = new UserRequest();
        request.setFullName("Juan Perez");
        request.setEmail("juan@test.com");
        request.setPhone("999999999");
        request.setRole("CLIENTE");

        when(repository.existsByEmail("juan@test.com")).thenReturn(true);


        var exception = assertThrows(
                DuplicateResourceException.class,
                () -> userService.create(request)
        );


        assertEquals("Ya existe un usuario con ese correo", exception.getMessage());

        verify(repository, times(1)).existsByEmail("juan@test.com");
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void noDebeCrearUsuarioConRolInvalido() {
        // Given
        UserRequest request = new UserRequest();
        request.setFullName("Juan Perez");
        request.setEmail("juan@test.com");
        request.setPhone("999999999");
        request.setRole("VENDEDOR");

        when(repository.existsByEmail("juan@test.com")).thenReturn(false);


        var exception = assertThrows(
                BusinessRuleException.class,
                () -> userService.create(request)
        );


        assertEquals("Rol no válido. Debe ser CLIENTE, DUENIO o ADMIN", exception.getMessage());

        verify(repository, times(1)).existsByEmail("juan@test.com");
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void debeBuscarUsuarioPorId() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setFullName("Juan Perez");
        user.setEmail("juan@test.com");
        user.setPhone("999999999");
        user.setRole("CLIENTE");
        user.setStatus("ACTIVO");

        when(repository.findById(1L)).thenReturn(Optional.of(user));


        UserResponse response = userService.getById(1L);


        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan Perez", response.getFullName());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void debeLanzarErrorCuandoUsuarioNoExiste() {

        when(repository.findById(99L)).thenReturn(Optional.empty());


        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getById(99L)
        );


        assertEquals("Usuario no encontrado con id: 99", exception.getMessage());

        verify(repository, times(1)).findById(99L);
    }

    @Test
    void debeDesactivarUsuario() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setFullName("Juan Perez");
        user.setEmail("juan@test.com");
        user.setPhone("999999999");
        user.setRole("CLIENTE");
        user.setStatus("ACTIVO");

        when(repository.findById(1L)).thenReturn(Optional.of(user));


        userService.delete(1L);


        assertEquals("INACTIVO", user.getStatus());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(user);
    }
}