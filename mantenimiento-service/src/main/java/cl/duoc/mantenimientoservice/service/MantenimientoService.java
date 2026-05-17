package cl.duoc.mantenimientoservice.service;

import cl.duoc.mantenimientoservice.dto.MantenimientoRequest;
import cl.duoc.mantenimientoservice.dto.MantenimientoResponse;
import cl.duoc.mantenimientoservice.exception.ResourceNotFoundException;
import cl.duoc.mantenimientoservice.model.Mantenimiento;
import cl.duoc.mantenimientoservice.repository.MantenimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MantenimientoService {

    private final MantenimientoRepository repository;

    public MantenimientoResponse create(MantenimientoRequest request) {
        log.info("Creando mantenimiento para canchaId: {}", request.getCanchaId());
        Mantenimiento entity = Mantenimiento.builder()
                .canchaId(request.getCanchaId())
                .descripcion(request.getDescripcion())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .estado(request.getEstado())
                .tecnico(request.getTecnico())
                .build();
        Mantenimiento saved = repository.save(entity);
        log.info("Mantenimiento creado con id: {}", saved.getId());
        return toResponse(saved);
    }

    public List<MantenimientoResponse> getAll() {
        log.info("Obteniendo todos los mantenimientos");
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MantenimientoResponse getById(Long id) {
        log.info("Buscando mantenimiento con id: {}", id);
        Mantenimiento entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado con id: " + id));
        return toResponse(entity);
    }

    public MantenimientoResponse update(Long id, MantenimientoRequest request) {
        log.info("Actualizando mantenimiento con id: {}", id);
        Mantenimiento entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado con id: " + id));
        entity.setCanchaId(request.getCanchaId());
        entity.setDescripcion(request.getDescripcion());
        entity.setFechaInicio(request.getFechaInicio());
        entity.setFechaFin(request.getFechaFin());
        entity.setEstado(request.getEstado());
        entity.setTecnico(request.getTecnico());
        Mantenimiento updated = repository.save(entity);
        log.info("Mantenimiento actualizado con id: {}", updated.getId());
        return toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Eliminando mantenimiento con id: {}", id);
        Mantenimiento entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado con id: " + id));
        repository.delete(entity);
        log.info("Mantenimiento eliminado con id: {}", id);
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
                .build();
    }
}