package cl.duoc.reservaservice.service;

import cl.duoc.reservaservice.dto.ReservaRequest;
import cl.duoc.reservaservice.dto.ReservaResponse;
import cl.duoc.reservaservice.exception.ResourceNotFoundException;
import cl.duoc.reservaservice.model.Reserva;
import cl.duoc.reservaservice.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaResponse create(ReservaRequest request) {
        log.info("Creando reserva para usuarioId={}, canchaId={}",
                request.getUsuarioId(), request.getCanchaId());
        Reserva reserva = Reserva.builder()
                .usuarioId(request.getUsuarioId())
                .canchaId(request.getCanchaId())
                .horarioId(request.getHorarioId())
                .fechaReserva(request.getFechaReserva())
                .estado(request.getEstado())
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con id: " + id));
        return toResponse(reserva);
    }

    public ReservaResponse update(Long id, ReservaRequest request) {
        log.info("Actualizando reserva con id={}", id);
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con id: " + id));
        reserva.setUsuarioId(request.getUsuarioId());
        reserva.setCanchaId(request.getCanchaId());
        reserva.setHorarioId(request.getHorarioId());
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setEstado(request.getEstado());
        Reserva updated = reservaRepository.save(reserva);
        log.info("Reserva actualizada con id={}", updated.getId());
        return toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Eliminando reserva con id={}", id);
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reserva no encontrada con id: " + id));
        reservaRepository.delete(reserva);
        log.info("Reserva eliminada con id={}", id);
    }

    private ReservaResponse toResponse(Reserva reserva) {
        return ReservaResponse.builder()
                .id(reserva.getId())
                .usuarioId(reserva.getUsuarioId())
                .canchaId(reserva.getCanchaId())
                .horarioId(reserva.getHorarioId())
                .fechaReserva(reserva.getFechaReserva())
                .estado(reserva.getEstado())
                .build();
    }
}