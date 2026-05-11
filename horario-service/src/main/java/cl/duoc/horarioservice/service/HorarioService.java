package cl.duoc.horarioservice.service;

import cl.duoc.horarioservice.dto.HorarioRequest;
import cl.duoc.horarioservice.dto.HorarioResponse;
import cl.duoc.horarioservice.exception.ResourceNotFoundException;
import cl.duoc.horarioservice.model.Horario;
import cl.duoc.horarioservice.repository.HorarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioService {

    private static final Logger log = LoggerFactory.getLogger(HorarioService.class);

    private final HorarioRepository repository;

    public HorarioService(HorarioRepository repository) {
        this.repository = repository;
    }

    public HorarioResponse create(HorarioRequest request) {

        log.info("Creando horario para cancha id: {}", request.getCanchaId());

        Horario horario = new Horario();

        horario.setCanchaId(request.getCanchaId());
        horario.setDayOfWeek(request.getDayOfWeek());
        horario.setStartTime(request.getStartTime());
        horario.setEndTime(request.getEndTime());
        horario.setAvailable(request.getAvailable());
        horario.setStatus(request.getStatus());

        horario.setCreatedAt(LocalDateTime.now());
        horario.setUpdatedAt(LocalDateTime.now());

        Horario saved = repository.save(horario);

        log.info("Horario creado correctamente con id: {}", saved.getId());

        return mapToResponse(saved);
    }

    public List<HorarioResponse> getAll() {

        log.info("Consultando horarios");

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public HorarioResponse getById(Long id) {

        log.info("Buscando horario con id: {}", id);

        Horario horario = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Horario no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Horario no encontrado con id: " + id);
                });

        return mapToResponse(horario);
    }

    public HorarioResponse update(Long id, HorarioRequest request) {

        log.info("Actualizando horario con id: {}", id);

        Horario horario = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Horario no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Horario no encontrado con id: " + id);
                });

        horario.setCanchaId(request.getCanchaId());
        horario.setDayOfWeek(request.getDayOfWeek());
        horario.setStartTime(request.getStartTime());
        horario.setEndTime(request.getEndTime());
        horario.setAvailable(request.getAvailable());
        horario.setStatus(request.getStatus());

        horario.setUpdatedAt(LocalDateTime.now());

        Horario updated = repository.save(horario);

        log.info("Horario actualizado correctamente con id: {}", updated.getId());

        return mapToResponse(updated);
    }

    public void delete(Long id) {

        log.info("Eliminando horario con id: {}", id);

        Horario horario = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Horario no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Horario no encontrado con id: " + id);
                });

        repository.delete(horario);

        log.info("Horario eliminado correctamente con id: {}", id);
    }

    private HorarioResponse mapToResponse(Horario horario) {

        return new HorarioResponse(
                horario.getId(),
                horario.getCanchaId(),
                horario.getDayOfWeek(),
                horario.getStartTime(),
                horario.getEndTime(),
                horario.getAvailable(),
                horario.getStatus(),
                horario.getCreatedAt(),
                horario.getUpdatedAt()
        );
    }
}