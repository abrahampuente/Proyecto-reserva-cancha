package cl.duoc.horarioservice.service;

import cl.duoc.horarioservice.client.CanchaClient;
import cl.duoc.horarioservice.dto.HorarioRequest;
import cl.duoc.horarioservice.dto.HorarioResponse;
import cl.duoc.horarioservice.exception.BusinessRuleException;
import cl.duoc.horarioservice.exception.ResourceNotFoundException;
import cl.duoc.horarioservice.model.Horario;
import cl.duoc.horarioservice.repository.HorarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class HorarioServiceTest {

    @Mock
    private HorarioRepository repository;

    @Mock
    private CanchaClient canchaClient;

    @InjectMocks
    private HorarioService horarioService;

    @Test
    void debeCrearHorarioCorrectamente() {
        HorarioRequest request = new HorarioRequest();
        request.setCanchaId(1L);
        request.setDayOfWeek("LUNES");
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(11, 0));
        request.setAvailable(true);

        Horario saved = new Horario();
        saved.setId(1L);
        saved.setCanchaId(1L);
        saved.setDayOfWeek("LUNES");
        saved.setStartTime(LocalTime.of(10, 0));
        saved.setEndTime(LocalTime.of(11, 0));
        saved.setAvailable(true);
        saved.setStatus("ACTIVO");

        when(repository.findByCanchaIdAndDayOfWeekAndStatus(1L, "LUNES", "ACTIVO"))
                .thenReturn(List.of());
        when(repository.save(any(Horario.class))).thenReturn(saved);

        HorarioResponse response = horarioService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("LUNES", response.getDayOfWeek());
        assertEquals("ACTIVO", response.getStatus());

        verify(canchaClient, times(1)).validateCanchaExists(1L);
        verify(repository, times(1)).save(any(Horario.class));
    }

    @Test
    void noDebeCrearHorarioConDiaInvalido() {
        HorarioRequest request = new HorarioRequest();
        request.setCanchaId(1L);
        request.setDayOfWeek("FUNDAY");
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(11, 0));

        var exception = assertThrows(
                BusinessRuleException.class,
                () -> horarioService.create(request)
        );

        assertEquals("Día de la semana no válido", exception.getMessage());
        verify(repository, never()).save(any(Horario.class));
    }

    @Test
    void noDebeCrearHorarioConHoraInicioMayor() {
        HorarioRequest request = new HorarioRequest();
        request.setCanchaId(1L);
        request.setDayOfWeek("LUNES");
        request.setStartTime(LocalTime.of(12, 0));
        request.setEndTime(LocalTime.of(11, 0));

        var exception = assertThrows(
                BusinessRuleException.class,
                () -> horarioService.create(request)
        );

        assertEquals("La hora de inicio debe ser menor a la hora de término", exception.getMessage());
        verify(repository, never()).save(any(Horario.class));
    }

    @Test
    void noDebeCrearHorarioSolapado() {
        HorarioRequest request = new HorarioRequest();
        request.setCanchaId(1L);
        request.setDayOfWeek("LUNES");
        request.setStartTime(LocalTime.of(10, 30));
        request.setEndTime(LocalTime.of(11, 30));

        Horario existente = new Horario();
        existente.setId(1L);
        existente.setCanchaId(1L);
        existente.setDayOfWeek("LUNES");
        existente.setStartTime(LocalTime.of(10, 0));
        existente.setEndTime(LocalTime.of(11, 0));
        existente.setStatus("ACTIVO");

        when(repository.findByCanchaIdAndDayOfWeekAndStatus(1L, "LUNES", "ACTIVO"))
                .thenReturn(List.of(existente));

        var exception = assertThrows(
                BusinessRuleException.class,
                () -> horarioService.create(request)
        );

        assertEquals("Ya existe un horario activo que se cruza con ese rango", exception.getMessage());
        verify(repository, never()).save(any(Horario.class));
    }

    @Test
    void debeBuscarHorarioPorId() {
        Horario horario = new Horario();
        horario.setId(1L);
        horario.setCanchaId(1L);
        horario.setDayOfWeek("LUNES");
        horario.setStartTime(LocalTime.of(10, 0));
        horario.setEndTime(LocalTime.of(11, 0));
        horario.setAvailable(true);
        horario.setStatus("ACTIVO");

        when(repository.findById(1L)).thenReturn(Optional.of(horario));

        HorarioResponse response = horarioService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("LUNES", response.getDayOfWeek());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void debeLanzarErrorCuandoHorarioNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> horarioService.getById(99L)
        );

        assertEquals("Horario no encontrado con id: 99", exception.getMessage());
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void debeDesactivarHorario() {
        Horario horario = new Horario();
        horario.setId(1L);
        horario.setCanchaId(1L);
        horario.setDayOfWeek("LUNES");
        horario.setStartTime(LocalTime.of(10, 0));
        horario.setEndTime(LocalTime.of(11, 0));
        horario.setAvailable(true);
        horario.setStatus("ACTIVO");

        when(repository.findById(1L)).thenReturn(Optional.of(horario));

        horarioService.delete(1L);

        assertEquals("INACTIVO", horario.getStatus());
        assertFalse(horario.getAvailable());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(horario);
    }
}