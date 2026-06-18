package cl.duoc.canchaservice.service;

import cl.duoc.canchaservice.client.RecintoClient;
import cl.duoc.canchaservice.dto.CanchaRequest;
import cl.duoc.canchaservice.dto.CanchaResponse;
import cl.duoc.canchaservice.exception.BusinessRuleException;
import cl.duoc.canchaservice.exception.ResourceNotFoundException;
import cl.duoc.canchaservice.model.Cancha;
import cl.duoc.canchaservice.repository.CanchaRepository;
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
class CanchaServiceTest {

    @Mock
    private CanchaRepository repository;

    @Mock
    private RecintoClient recintoClient;

    @InjectMocks
    private CanchaService canchaService;

    @Test
    void debeCrearCanchaCorrectamente() {
        // Given
        CanchaRequest request = new CanchaRequest();
        request.setName("Cancha 1");
        request.setSportType("FUTBOL");
        request.setSurfaceType("PASTO SINTETICO");
        request.setCapacity(10);
        request.setRecintoId(1L);

        Cancha saved = new Cancha();
        saved.setId(1L);
        saved.setName("Cancha 1");
        saved.setSportType("FUTBOL");
        saved.setSurfaceType("PASTO SINTETICO");
        saved.setCapacity(10);
        saved.setRecintoId(1L);
        saved.setStatus("ACTIVA");

        when(repository.existsByNameAndRecintoIdAndStatus("Cancha 1", 1L, "ACTIVA"))
                .thenReturn(false);

        when(repository.save(any(Cancha.class))).thenReturn(saved);

        // When
        CanchaResponse response = canchaService.create(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Cancha 1", response.getName());
        assertEquals("ACTIVA", response.getStatus());

        verify(recintoClient, times(1)).validateRecintoExists(1L);
        verify(repository, times(1)).save(any(Cancha.class));
    }

    @Test
    void noDebeCrearCanchaConCapacidadInvalida() {
        // Given
        CanchaRequest request = new CanchaRequest();
        request.setName("Cancha 1");
        request.setSportType("FUTBOL");
        request.setSurfaceType("PASTO SINTETICO");
        request.setCapacity(0);
        request.setRecintoId(1L);

        // When
        var exception = assertThrows(
                BusinessRuleException.class,
                () -> canchaService.create(request)
        );

        // Then
        assertEquals("La capacidad debe ser mayor a cero", exception.getMessage());

        verify(repository, never()).save(any(Cancha.class));
    }

    @Test
    void noDebeCrearCanchaDuplicada() {
        // Given
        CanchaRequest request = new CanchaRequest();
        request.setName("Cancha 1");
        request.setSportType("FUTBOL");
        request.setSurfaceType("PASTO SINTETICO");
        request.setCapacity(10);
        request.setRecintoId(1L);

        when(repository.existsByNameAndRecintoIdAndStatus("Cancha 1", 1L, "ACTIVA"))
                .thenReturn(true);

        // When
        var exception = assertThrows(
                BusinessRuleException.class,
                () -> canchaService.create(request)
        );

        // Then
        assertEquals("Ya existe una cancha activa con ese nombre en el recinto", exception.getMessage());

        verify(repository, never()).save(any(Cancha.class));
    }

    @Test
    void debeBuscarCanchaPorId() {
        // Given
        Cancha cancha = new Cancha();
        cancha.setId(1L);
        cancha.setName("Cancha 1");
        cancha.setSportType("FUTBOL");
        cancha.setSurfaceType("PASTO SINTETICO");
        cancha.setCapacity(10);
        cancha.setRecintoId(1L);
        cancha.setStatus("ACTIVA");

        when(repository.findById(1L)).thenReturn(Optional.of(cancha));

        // When
        CanchaResponse response = canchaService.getById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Cancha 1", response.getName());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void debeLanzarErrorCuandoCanchaNoExiste() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When
        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> canchaService.getById(99L)
        );

        // Then
        assertEquals("Cancha no encontrada con id: 99", exception.getMessage());

        verify(repository, times(1)).findById(99L);
    }

    @Test
    void debeDesactivarCancha() {
        // Given
        Cancha cancha = new Cancha();
        cancha.setId(1L);
        cancha.setName("Cancha 1");
        cancha.setSportType("FUTBOL");
        cancha.setSurfaceType("PASTO SINTETICO");
        cancha.setCapacity(10);
        cancha.setRecintoId(1L);
        cancha.setStatus("ACTIVA");

        when(repository.findById(1L)).thenReturn(Optional.of(cancha));

        // When
        canchaService.delete(1L);

        // Then
        assertEquals("INACTIVA", cancha.getStatus());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(cancha);
    }
}