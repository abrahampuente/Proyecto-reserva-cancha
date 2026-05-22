package cl.duoc.notificacionservice.service;

import cl.duoc.notificacionservice.dto.NotificacionRequest;
import cl.duoc.notificacionservice.dto.NotificacionResponse;
import cl.duoc.notificacionservice.exception.BusinessRuleException;
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

        validateNotificacion(request);

        Notificacion entity = Notificacion.builder()
                .titulo(request.getTitulo())
                .mensaje(request.getMensaje())
                .destinatario(request.getDestinatario())
                .tipo(request.getTipo().toUpperCase())
                .estado(request.getEstado() != null ? request.getEstado().toUpperCase() : "ENVIADA")
                .leida(request.getLeida() != null ? request.getLeida() : false)
                .build();

        return toResponse(repository.save(entity));
    }

    public List<NotificacionResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public NotificacionResponse getById(Long id) {
        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id: " + id));

        return toResponse(entity);
    }

    public NotificacionResponse update(Long id, NotificacionRequest request) {
        validateNotificacion(request);

        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id: " + id));

        entity.setTitulo(request.getTitulo());
        entity.setMensaje(request.getMensaje());
        entity.setDestinatario(request.getDestinatario());
        entity.setTipo(request.getTipo().toUpperCase());
        entity.setLeida(request.getLeida() != null ? request.getLeida() : entity.getLeida());

        if (request.getEstado() != null) {
            entity.setEstado(request.getEstado().toUpperCase());
        }

        return toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id: " + id));

        entity.setEstado("FALLIDA");
        repository.save(entity);
    }

    public boolean existsById(Long id) {
        return repository.existsByIdAndEstadoNot(id, "FALLIDA");
    }

    private void validateNotificacion(NotificacionRequest request) {
        if (request.getTitulo() == null || request.getTitulo().isBlank()) {
            throw new BusinessRuleException("El título es obligatorio");
        }

        if (request.getMensaje() == null || request.getMensaje().isBlank()) {
            throw new BusinessRuleException("El mensaje es obligatorio");
        }

        if (request.getMensaje().length() > 500) {
            throw new BusinessRuleException("El mensaje no puede superar los 500 caracteres");
        }

        if (request.getDestinatario() == null || request.getDestinatario().isBlank()) {
            throw new BusinessRuleException("El destinatario es obligatorio");
        }

        validateTipo(request.getTipo());

        if (request.getEstado() != null) {
            validateEstado(request.getEstado());
        }
    }

    private void validateTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new BusinessRuleException("El tipo de notificación es obligatorio");
        }

        String tipoNormalizado = tipo.toUpperCase();

        if (!tipoNormalizado.equals("EMAIL")
                && !tipoNormalizado.equals("SMS")
                && !tipoNormalizado.equals("PUSH")) {
            throw new BusinessRuleException("Tipo de notificación no válido");
        }
    }

    private void validateEstado(String estado) {
        String estadoNormalizado = estado.toUpperCase();

        if (!estadoNormalizado.equals("PENDIENTE")
                && !estadoNormalizado.equals("ENVIADA")
                && !estadoNormalizado.equals("FALLIDA")) {
            throw new BusinessRuleException("Estado de notificación no válido");
        }
    }

    private NotificacionResponse toResponse(Notificacion entity) {
        return NotificacionResponse.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .mensaje(entity.getMensaje())
                .destinatario(entity.getDestinatario())
                .tipo(entity.getTipo())
                .estado(entity.getEstado())
                .leida(entity.getLeida())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }
}