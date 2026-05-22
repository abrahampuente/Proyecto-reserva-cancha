package cl.duoc.mantenimientoservice.service;

import cl.duoc.mantenimientoservice.client.CanchaClient;
import cl.duoc.mantenimientoservice.dto.MantenimientoRequest;
import cl.duoc.mantenimientoservice.dto.MantenimientoResponse;
import cl.duoc.mantenimientoservice.exception.BusinessRuleException;
import cl.duoc.mantenimientoservice.exception.ResourceNotFoundException;
import cl.duoc.mantenimientoservice.model.Mantenimiento;
import cl.duoc.mantenimientoservice.repository.MantenimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MantenimientoService {

    private final MantenimientoRepository repository;
    private final CanchaClient canchaClient;

    public MantenimientoResponse create(MantenimientoRequest request) {
        log.info("Creando mantenimiento para canchaId: {}", request.getCanchaId());

        canchaClient.validateCanchaExists(request.getCanchaId());
        validateMantenimiento(request);
        validateSolapamiento(request, null);

        Mantenimiento entity = Mantenimiento.builder()
                .canchaId(request.getCanchaId())
                .descripcion(request.getDescripcion())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .estado(request.getEstado() != null ? request.getEstado() : "PENDIENTE")
                .tecnico(request.getTecnico())
                .build();

        Mantenimiento saved = repository.save(entity);
        return toResponse(saved);
    }

    public List<MantenimientoResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MantenimientoResponse getById(Long id) {
        Mantenimiento entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado con id: " + id));

        return toResponse(entity);
    }

    public MantenimientoResponse update(Long id, MantenimientoRequest request) {
        Mantenimiento entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado con id: " + id));

        canchaClient.validateCanchaExists(request.getCanchaId());
        validateMantenimiento(request);
        validateSolapamiento(request, id);

        entity.setCanchaId(request.getCanchaId());
        entity.setDescripcion(request.getDescripcion());
        entity.setFechaInicio(request.getFechaInicio());
        entity.setFechaFin(request.getFechaFin());
        entity.setEstado(request.getEstado() != null ? request.getEstado() : entity.getEstado());
        entity.setTecnico(request.getTecnico());

        Mantenimiento updated = repository.save(entity);
        return toResponse(updated);
    }

    public void delete(Long id) {
        Mantenimiento entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado con id: " + id));

        entity.setEstado("FINALIZADO");
        repository.save(entity);
    }

    public boolean existsById(Long id) {
        return repository.existsByIdAndEstadoNot(id, "FINALIZADO");
    }

    private void validateMantenimiento(MantenimientoRequest request) {
        if (request.getFechaInicio() == null) {
            throw new BusinessRuleException("La fecha de inicio es obligatoria");
        }

        if (request.getFechaFin() != null && request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new BusinessRuleException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        if (request.getTecnico() == null || request.getTecnico().isBlank()) {
            throw new BusinessRuleException("El técnico es obligatorio");
        }

        if (request.getDescripcion() == null || request.getDescripcion().isBlank()) {
            throw new BusinessRuleException("La descripción es obligatoria");
        }

        if (request.getDescripcion().length() > 500) {
            throw new BusinessRuleException("La descripción no puede superar los 500 caracteres");
        }

        if (request.getEstado() != null) {
            validateEstado(request.getEstado());
        }
    }

    private void validateEstado(String estado) {
        if (!estado.equals("PENDIENTE")
                && !estado.equals("EN_PROCESO")
                && !estado.equals("FINALIZADO")) {
            throw new BusinessRuleException("Estado de mantenimiento no válido");
        }
    }

    private void validateSolapamiento(MantenimientoRequest request, Long mantenimientoIdActual) {
        LocalDate finNuevo = request.getFechaFin() != null ? request.getFechaFin() : request.getFechaInicio();

        List<Mantenimiento> mantenimientos = repository.findByCanchaIdAndEstadoNot(
                request.getCanchaId(),
                "FINALIZADO"
        );

        for (Mantenimiento existente : mantenimientos) {
            if (mantenimientoIdActual != null && existente.getId().equals(mantenimientoIdActual)) {
                continue;
            }

            LocalDate finExistente = existente.getFechaFin() != null
                    ? existente.getFechaFin()
                    : existente.getFechaInicio();

            boolean solapado = !request.getFechaInicio().isAfter(finExistente)
                    && !finNuevo.isBefore(existente.getFechaInicio());

            if (solapado) {
                throw new BusinessRuleException("Ya existe un mantenimiento activo para esa cancha en esas fechas");
            }
        }
    }

    private MantenimientoResponse toResponse(Mantenimiento entity) {
        return MantenimientoResponse.builder()
                .id(entity.getId())
                .canchaId(entity.getCanchaId())
                .descripcion(entity.getDescripcion())
                .fechaInicio(entity.getFechaInicio())
                .fechaFin(entity.getFechaFin())
                .estado(entity.getEstado())
                .tecnico(entity.getTecnico())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }
}