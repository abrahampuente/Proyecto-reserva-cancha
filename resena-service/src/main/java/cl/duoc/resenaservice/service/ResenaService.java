package cl.duoc.resenaservice.service;

import cl.duoc.resenaservice.dto.ResenaRequest;
import cl.duoc.resenaservice.dto.ResenaResponse;
import cl.duoc.resenaservice.exception.ResourceNotFoundException;
import cl.duoc.resenaservice.model.Resena;
import cl.duoc.resenaservice.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository repository;

    public ResenaResponse create(ResenaRequest request) {
        log.info("Creando reseña para canchaId: {}", request.getCanchaId());
        Resena entity = Resena.builder()
                .usuarioId(request.getUsuarioId())
                .canchaId(request.getCanchaId())
                .comentario(request.getComentario())
                .calificacion(request.getCalificacion())
                .build();
        Resena saved = repository.save(entity);
        log.info("Reseña creada con id: {}", saved.getId());
        return toResponse(saved);
    }

    public List<ResenaResponse> getAll() {
        log.info("Obteniendo todas las reseñas");
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ResenaResponse getById(Long id) {
        log.info("Buscando reseña con id: {}", id);
        Resena entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada con id: " + id));
        return toResponse(entity);
    }

    public ResenaResponse update(Long id, ResenaRequest request) {
        log.info("Actualizando reseña con id: {}", id);
        Resena entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada con id: " + id));
        entity.setUsuarioId(request.getUsuarioId());
        entity.setCanchaId(request.getCanchaId());
        entity.setComentario(request.getComentario());
        entity.setCalificacion(request.getCalificacion());
        Resena updated = repository.save(entity);
        log.info("Reseña actualizada con id: {}", updated.getId());
        return toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Eliminando reseña con id: {}", id);
        Resena entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada con id: " + id));
        repository.delete(entity);
        log.info("Reseña eliminada con id: {}", id);
    }

    private ResenaResponse toResponse(Resena entity) {
        return ResenaResponse.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuarioId())
                .canchaId(entity.getCanchaId())
                .comentario(entity.getComentario())
                .calificacion(entity.getCalificacion())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}
