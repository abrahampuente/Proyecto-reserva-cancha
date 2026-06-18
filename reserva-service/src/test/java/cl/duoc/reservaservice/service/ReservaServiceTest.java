package cl.duoc.reservaservice.service;

import cl.duoc.reservaservice.client.CanchaClient;
import cl.duoc.reservaservice.client.HorarioClient;
import cl.duoc.reservaservice.client.UserClient;
import cl.duoc.reservaservice.dto.ReservaRequest;
import cl.duoc.reservaservice.dto.ReservaResponse;
import cl.duoc.reservaservice.model.Reserva;
import cl.duoc.reservaservice.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private CanchaClient canchaClient;

    @Mock
    private HorarioClient horarioClient;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void debeCrearReservaCorrectamente() {
        // Given
        ReservaRequest request = new ReservaRequest();
        request.setUsuarioId(1L);
        request.setCanchaId(1L);
        request.setHorarioId(1L);
        request.setFechaReserva(LocalDateTime.now().plusDays(1));

        Reserva reservaGuardada = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .horarioId(1L)
                .fechaReserva(request.getFechaReserva())
                .estado("PENDIENTE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(reservaRepository.existsByCanchaIdAndHorarioIdAndFechaReservaAndEstadoNot(
                anyLong(), anyLong(), any(LocalDateTime.class), anyString()
        )).thenReturn(false);

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaGuardada);

        // When
        ReservaResponse response = reservaService.create(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PENDIENTE", response.getEstado());

        verify(userClient, times(1)).validateClienteExists(1L);
        verify(canchaClient, times(1)).validateCanchaExists(1L);
        verify(horarioClient, times(1)).validateHorarioExists(1L);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }
    @Test
    void noDebeCrearReservaConFechaPasada() {
        // Given
        ReservaRequest request = new ReservaRequest();
        request.setUsuarioId(1L);
        request.setCanchaId(1L);
        request.setHorarioId(1L);
        request.setFechaReserva(LocalDateTime.now().minusDays(1));

        // When
        var exception = assertThrows(
                cl.duoc.reservaservice.exception.BusinessRuleException.class,
                () -> reservaService.create(request)
        );

        // Then
        assertEquals("No se puede crear una reserva en una fecha pasada", exception.getMessage());

        verify(reservaRepository, never()).save(any(Reserva.class));
    }
    @Test
    void noDebeCrearReservaDuplicada() {
        // Given
        ReservaRequest request = new ReservaRequest();
        request.setUsuarioId(1L);
        request.setCanchaId(1L);
        request.setHorarioId(1L);
        request.setFechaReserva(LocalDateTime.now().plusDays(1));

        when(reservaRepository.existsByCanchaIdAndHorarioIdAndFechaReservaAndEstadoNot(
                anyLong(), anyLong(), any(LocalDateTime.class), anyString()
        )).thenReturn(true);

        // When
        var exception = assertThrows(
                cl.duoc.reservaservice.exception.BusinessRuleException.class,
                () -> reservaService.create(request)
        );

        // Then
        assertEquals("Ya existe una reserva activa para esa cancha, horario y fecha", exception.getMessage());

        verify(reservaRepository, never()).save(any(Reserva.class));
    }
    @Test
    void debeBuscarReservaPorId() {

        Reserva reserva = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .horarioId(1L)
                .fechaReserva(LocalDateTime.now().plusDays(1))
                .estado("PENDIENTE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(reservaRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reserva));

        ReservaResponse response = reservaService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());

        verify(reservaRepository, times(1)).findById(1L);
    }
    @Test
    void debeCancelarReserva() {

        Reserva reserva = Reserva.builder()
                .id(1L)
                .usuarioId(1L)
                .canchaId(1L)
                .horarioId(1L)
                .fechaReserva(LocalDateTime.now().plusDays(1))
                .estado("PENDIENTE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(reservaRepository.findById(1L))
                .thenReturn(java.util.Optional.of(reserva));

        reservaService.delete(1L);

        assertEquals("CANCELADA", reserva.getEstado());

        verify(reservaRepository, times(1)).save(reserva);
    }
    @Test
    void debeLanzarErrorCuandoReservaNoExiste() {


        when(reservaRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());


        var exception = assertThrows(
                cl.duoc.reservaservice.exception.ResourceNotFoundException.class,
                () -> reservaService.getById(99L)
        );


        assertEquals("Reserva no encontrada con id: 99", exception.getMessage());

        verify(reservaRepository, times(1)).findById(99L);
    }

}