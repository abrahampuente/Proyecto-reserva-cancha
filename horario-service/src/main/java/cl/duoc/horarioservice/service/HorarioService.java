package cl.duoc.horarioservice.service;

import cl.duoc.horarioservice.client.CanchaClient;
import cl.duoc.horarioservice.dto.HorarioRequest;
import cl.duoc.horarioservice.dto.HorarioResponse;
import cl.duoc.horarioservice.exception.BusinessRuleException;
import cl.duoc.horarioservice.exception.ResourceNotFoundException;
import cl.duoc.horarioservice.model.Horario;
import cl.duoc.horarioservice.repository.HorarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioService {

    private static final Logger log = LoggerFactory.getLogger(HorarioService.class);

    private final HorarioRepository repository;
    private final CanchaClient canchaClient;

    public HorarioService(HorarioRepository repository, CanchaClient canchaClient) {
        this.repository = repository;
        this.canchaClient = canchaClient;
    }

    public HorarioResponse create(HorarioRequest request) {
        log.info("Creando horario para cancha id: {}", request.getCanchaId());

        canchaClient.validateCanchaExists(request.getCanchaId());
        validateHorario(request);
        validateHorarioSolapado(request, null);

        Horario horario = new Horario();
        horario.setCanchaId(request.getCanchaId());
        horario.setDayOfWeek(request.getDayOfWeek().toUpperCase());
        horario.setStartTime(request.getStartTime());
        horario.setEndTime(request.getEndTime());
        horario.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        horario.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVO");

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

        canchaClient.validateCanchaExists(request.getCanchaId());
        validateHorario(request);
        validateHorarioSolapado(request, id);

        horario.setCanchaId(request.getCanchaId());
        horario.setDayOfWeek(request.getDayOfWeek().toUpperCase());
        horario.setStartTime(request.getStartTime());
        horario.setEndTime(request.getEndTime());
        horario.setAvailable(request.getAvailable() != null ? request.getAvailable() : horario.getAvailable());

        if (request.getStatus() != null) {
            validateStatus(request.getStatus());
            horario.setStatus(request.getStatus());
        }

        Horario updated = repository.save(horario);
        log.info("Horario actualizado correctamente con id: {}", updated.getId());

        return mapToResponse(updated);
    }

    public void delete(Long id) {
        log.info("Desactivando horario con id: {}", id);

        Horario horario = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Horario no encontrado con id: {}", id);
                    return new ResourceNotFoundException("Horario no encontrado con id: " + id);
                });

        horario.setStatus("INACTIVO");
        horario.setAvailable(false);
        repository.save(horario);

        log.info("Horario desactivado correctamente con id: {}", id);
    }

    public boolean existsById(Long id) {
        return repository.existsByIdAndStatus(id, "ACTIVO");
    }

    private void validateHorario(HorarioRequest request) {
        if (request.getDayOfWeek() == null || request.getDayOfWeek().isBlank()) {
            throw new BusinessRuleException("El día de la semana es obligatorio");
        }

        String dia = request.getDayOfWeek().toUpperCase();

        if (!dia.equals("LUNES")
                && !dia.equals("MARTES")
                && !dia.equals("MIERCOLES")
                && !dia.equals("JUEVES")
                && !dia.equals("VIERNES")
                && !dia.equals("SABADO")
                && !dia.equals("DOMINGO")) {
            throw new BusinessRuleException("Día de la semana no válido");
        }

        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new BusinessRuleException("La hora de inicio y término son obligatorias");
        }

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new BusinessRuleException("La hora de inicio debe ser menor a la hora de término");
        }

        if (request.getStatus() != null) {
            validateStatus(request.getStatus());
        }
    }

    private void validateHorarioSolapado(HorarioRequest request, Long horarioIdActual) {
        List<Horario> horarios = repository.findByCanchaIdAndDayOfWeekAndStatus(
                request.getCanchaId(),
                request.getDayOfWeek().toUpperCase(),
                "ACTIVO"
        );

        for (Horario existente : horarios) {
            if (horarioIdActual != null && existente.getId().equals(horarioIdActual)) {
                continue;
            }

            boolean solapado = request.getStartTime().isBefore(existente.getEndTime())
                    && request.getEndTime().isAfter(existente.getStartTime());

            if (solapado) {
                throw new BusinessRuleException("Ya existe un horario activo que se cruza con ese rango");
            }
        }
    }

    private void validateStatus(String status) {
        if (!status.equals("ACTIVO") && !status.equals("INACTIVO")) {
            throw new BusinessRuleException("Estado de horario no válido");
        }
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