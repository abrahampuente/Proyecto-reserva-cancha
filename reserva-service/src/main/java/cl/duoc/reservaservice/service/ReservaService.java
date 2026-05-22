package cl.duoc.reservaservice.service;
// Servicio principal para gestión de reservas con lógica CRUD completa

import cl.duoc.reservaservice.client.CanchaClient;
import cl.duoc.reservaservice.client.HorarioClient;
import cl.duoc.reservaservice.client.UserClient;
import cl.duoc.reservaservice.dto.ReservaRequest;
import cl.duoc.reservaservice.dto.ReservaResponse;
import cl.duoc.reservaservice.exception.BusinessRuleException;
import cl.duoc.reservaservice.exception.ResourceNotFoundException;
import cl.duoc.reservaservice.model.Reserva;
import cl.duoc.reservaservice.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UserClient userClient;
    private final CanchaClient canchaClient;
    private final HorarioClient horarioClient;

    public ReservaResponse create(ReservaRequest request) {
        log.info("Creando reserva para usuarioId={}, canchaId={}",
                request.getUsuarioId(), request.getCanchaId());

        userClient.validateClienteExists(request.getUsuarioId());
        canchaClient.validateCanchaExists(request.getCanchaId());
        horarioClient.validateHorarioExists(request.getHorarioId());
        validateReserva(request);

        validateReservaDuplicada(request);

        Reserva reserva = Reserva.builder()
                .usuarioId(request.getUsuarioId())
                .canchaId(request.getCanchaId())
                .horarioId(request.getHorarioId())
                .fechaReserva(request.getFechaReserva())
                .estado("PENDIENTE")
                .build();

        Reserva saved = reservaRepository.save(reserva);
        log.info("Reserva creada con id={}", saved.getId());

        return toResponse(saved);
    }

    public List<ReservaResponse> getAll() {
        log.info("Obteniendo todas las reservas");
        return reservaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ReservaResponse getById(Long id) {
        log.info("Buscando reserva con id={}", id);

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        return toResponse(reserva);
    }

    public ReservaResponse update(Long id, ReservaRequest request) {
        log.info("Actualizando reserva con id={}", id);

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        userClient.validateClienteExists(request.getUsuarioId());
        canchaClient.validateCanchaExists(request.getCanchaId());
        horarioClient.validateHorarioExists(request.getHorarioId());
        validateReserva(request);

        if (!reserva.getCanchaId().equals(request.getCanchaId())
                || !reserva.getHorarioId().equals(request.getHorarioId())
                || !reserva.getFechaReserva().equals(request.getFechaReserva())) {
            validateReservaDuplicada(request);
        }

        reserva.setUsuarioId(request.getUsuarioId());
        reserva.setCanchaId(request.getCanchaId());
        reserva.setHorarioId(request.getHorarioId());
        reserva.setFechaReserva(request.getFechaReserva());

        if (request.getEstado() != null) {
            validateEstado(request.getEstado());
            reserva.setEstado(request.getEstado());
        }

        Reserva updated = reservaRepository.save(reserva);
        log.info("Reserva actualizada con id={}", updated.getId());

        return toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Cancelando reserva con id={}", id);

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);

        log.info("Reserva cancelada con id={}", id);
    }

    public boolean existsById(Long id) {
        return reservaRepository.existsByIdAndEstadoNot(id, "CANCELADA");
    }

    private void validateReserva(ReservaRequest request) {
        if (request.getFechaReserva() == null) {
            throw new BusinessRuleException("La fecha de reserva es obligatoria");
        }

        if (request.getFechaReserva().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("No se puede crear una reserva en una fecha pasada");
        }

        if (request.getEstado() != null) {
            validateEstado(request.getEstado());
        }
    }

    private void validateReservaDuplicada(ReservaRequest request) {
        boolean existe = reservaRepository.existsByCanchaIdAndHorarioIdAndFechaReservaAndEstadoNot(
                request.getCanchaId(),
                request.getHorarioId(),
                request.getFechaReserva(),
                "CANCELADA"
        );

        if (existe) {
            throw new BusinessRuleException("Ya existe una reserva activa para esa cancha, horario y fecha");
        }
    }

    private void validateEstado(String estado) {
        if (!estado.equals("PENDIENTE")
                && !estado.equals("CONFIRMADA")
                && !estado.equals("CANCELADA")) {
            throw new BusinessRuleException("Estado de reserva no válido");
        }
    }

    private ReservaResponse toResponse(Reserva reserva) {
        return ReservaResponse.builder()
                .id(reserva.getId())
                .usuarioId(reserva.getUsuarioId())
                .canchaId(reserva.getCanchaId())
                .horarioId(reserva.getHorarioId())
                .fechaReserva(reserva.getFechaReserva())
                .estado(reserva.getEstado())
                .createdAt(reserva.getCreatedAt())
                .updatedAt(reserva.getUpdatedAt())
                .build();
    }
}
