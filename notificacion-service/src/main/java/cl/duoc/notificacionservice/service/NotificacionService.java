package cl.duoc.notificacionservice.service;

import cl.duoc.notificacionservice.dto.NotificacionRequest;
import cl.duoc.notificacionservice.dto.NotificacionResponse;
import cl.duoc.notificacionservice.exception.ResourceNotFoundException;
import cl.duoc.notificacionservice.model.Notificacion;
import cl.duoc.notificacionservice.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository repository;

    public NotificacionResponse create(NotificacionRequest request) {
        log.info("Creando notificación para destinatario: {}", request.getDestinatario());
        Notificacion entity = Notificacion.builder()
                .titulo(request.getTitulo())
                .mensaje(request.getMensaje())
                .destinatario(request.getDestinatario())
                .tipo(request.getTipo())
                .leida(request.getLeida() != null ? request.getLeida() : false)
                .build();
        Notificacion saved = repository.save(entity);
        log.info("Notificación creada con id: {}", saved.getId());
        return toResponse(saved);
    }

    public List<NotificacionResponse> getAll() {
        log.info("Obteniendo todas las notificaciones");
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public NotificacionResponse getById(Long id) {
        log.info("Buscando notificación con id: {}", id);
        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id: " + id));
        return toResponse(entity);
    }

    public NotificacionResponse update(Long id, NotificacionRequest request) {
        log.info("Actualizando notificación con id: {}", id);
        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id: " + id));
        entity.setTitulo(request.getTitulo());
        entity.setMensaje(request.getMensaje());
        entity.setDestinatario(request.getDestinatario());
        entity.setTipo(request.getTipo());
        entity.setLeida(request.getLeida() != null ? request.getLeida() : entity.getLeida());
        Notificacion updated = repository.save(entity);
        log.info("Notificación actualizada con id: {}", updated.getId());
        return toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Eliminando notificación con id: {}", id);
        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id: " + id));
        repository.delete(entity);
        log.info("Notificación eliminada con id: {}", id);
    }

    private NotificacionResponse toResponse(Notificacion entity) {
        return NotificacionResponse.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .mensaje(entity.getMensaje())
                .destinatario(entity.getDestinatario())
                .tipo(entity.getTipo())
                .leida(entity.getLeida())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}