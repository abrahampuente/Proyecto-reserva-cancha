package cl.duoc.resenaservice.service;

import cl.duoc.resenaservice.client.CanchaClient;
import cl.duoc.resenaservice.client.UserClient;
import cl.duoc.resenaservice.dto.ResenaRequest;
import cl.duoc.resenaservice.dto.ResenaResponse;
import cl.duoc.resenaservice.exception.BusinessRuleException;
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
    private final UserClient userClient;
    private final CanchaClient canchaClient;

    public ResenaResponse create(ResenaRequest request) {
        log.info("Creando reseña para canchaId: {}", request.getCanchaId());

        userClient.validateClienteOrAdminExists(request.getUsuarioId());
        canchaClient.validateCanchaExists(request.getCanchaId());
        validateResena(request);

        if (repository.existsByUsuarioIdAndCanchaIdAndEstado(
                request.getUsuarioId(),
                request.getCanchaId(),
                "ACTIVA")) {
            throw new BusinessRuleException("El usuario ya registró una reseña activa para esta cancha");
        }

        Resena entity = Resena.builder()
                .usuarioId(request.getUsuarioId())
                .canchaId(request.getCanchaId())
                .comentario(request.getComentario())
                .calificacion(request.getCalificacion())
                .estado("ACTIVA")
                .build();

        Resena saved = repository.save(entity);

        return toResponse(saved);
    }

    public List<ResenaResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ResenaResponse getById(Long id) {
        Resena entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reseña no encontrada con id: " + id));

        return toResponse(entity);
    }

    public ResenaResponse update(Long id, ResenaRequest request) {
        Resena entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reseña no encontrada con id: " + id));

        userClient.validateClienteOrAdminExists(request.getUsuarioId());
        canchaClient.validateCanchaExists(request.getCanchaId());
        validateResena(request);

        if ((!entity.getUsuarioId().equals(request.getUsuarioId())
                || !entity.getCanchaId().equals(request.getCanchaId()))
                && repository.existsByUsuarioIdAndCanchaIdAndEstado(
                request.getUsuarioId(),
                request.getCanchaId(),
                "ACTIVA")) {
            throw new BusinessRuleException("El usuario ya registró una reseña activa para esta cancha");
        }

        entity.setUsuarioId(request.getUsuarioId());
        entity.setCanchaId(request.getCanchaId());
        entity.setComentario(request.getComentario());
        entity.setCalificacion(request.getCalificacion());

        Resena updated = repository.save(entity);

        return toResponse(updated);
    }

    public void delete(Long id) {
        Resena entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reseña no encontrada con id: " + id));

        entity.setEstado("ELIMINADA");
        repository.save(entity);
    }

    private void validateResena(ResenaRequest request) {
        if (request.getCalificacion() == null || request.getCalificacion() < 1 || request.getCalificacion() > 5) {
            throw new BusinessRuleException("La calificación debe estar entre 1 y 5");
        }

        if (request.getComentario() == null || request.getComentario().isBlank()) {
            throw new BusinessRuleException("El comentario es obligatorio");
        }

        if (request.getComentario().length() > 500) {
            throw new BusinessRuleException("El comentario no puede superar los 500 caracteres");
        }
    }

    private ResenaResponse toResponse(Resena entity) {
        return ResenaResponse.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuarioId())
                .canchaId(entity.getCanchaId())
                .comentario(entity.getComentario())
                .calificacion(entity.getCalificacion())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }
}