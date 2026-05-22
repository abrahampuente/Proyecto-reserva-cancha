package cl.duoc.precioservice.service;

import cl.duoc.precioservice.client.CanchaClient;
import cl.duoc.precioservice.dto.PrecioRequest;
import cl.duoc.precioservice.dto.PrecioResponse;
import cl.duoc.precioservice.exception.BusinessRuleException;
import cl.duoc.precioservice.exception.ResourceNotFoundException;
import cl.duoc.precioservice.model.Precio;
import cl.duoc.precioservice.repository.PrecioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrecioService {

    private final PrecioRepository precioRepository;
    private final CanchaClient canchaClient;

    public PrecioResponse create(PrecioRequest request) {
        log.info("Creando precio para canchaId={}", request.getCanchaId());

        canchaClient.validateCanchaExists(request.getCanchaId());
        validatePrecio(request);

        if (precioRepository.existsByCanchaIdAndEstado(request.getCanchaId(), "ACTIVO")) {
            throw new BusinessRuleException("La cancha ya tiene un precio activo");
        }

        Precio precio = Precio.builder()
                .canchaId(request.getCanchaId())
                .valor(request.getValor())
                .moneda("CLP")
                .descripcion(request.getDescripcion())
                .estado(request.getEstado() != null ? request.getEstado() : "ACTIVO")
                .build();

        Precio saved = precioRepository.save(precio);
        log.info("Precio creado con id={}", saved.getId());

        return toResponse(saved);
    }

    public List<PrecioResponse> getAll() {
        log.info("Obteniendo todos los precios");
        return precioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PrecioResponse getById(Long id) {
        log.info("Buscando precio con id={}", id);

        Precio precio = precioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Precio no encontrado con id: " + id));

        return toResponse(precio);
    }

    public PrecioResponse update(Long id, PrecioRequest request) {
        log.info("Actualizando precio con id={}", id);

        Precio precio = precioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Precio no encontrado con id: " + id));

        canchaClient.validateCanchaExists(request.getCanchaId());
        validatePrecio(request);

        if (!precio.getCanchaId().equals(request.getCanchaId())
                && precioRepository.existsByCanchaIdAndEstado(request.getCanchaId(), "ACTIVO")) {
            throw new BusinessRuleException("La cancha ya tiene un precio activo");
        }

        precio.setCanchaId(request.getCanchaId());
        precio.setValor(request.getValor());
        precio.setMoneda("CLP");
        precio.setDescripcion(request.getDescripcion());

        if (request.getEstado() != null) {
            validateEstado(request.getEstado());
            precio.setEstado(request.getEstado());
        }

        Precio updated = precioRepository.save(precio);
        log.info("Precio actualizado con id={}", updated.getId());

        return toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Desactivando precio con id={}", id);

        Precio precio = precioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Precio no encontrado con id: " + id));

        precio.setEstado("INACTIVO");
        precioRepository.save(precio);

        log.info("Precio desactivado con id={}", id);
    }

    public boolean existsById(Long id) {
        return precioRepository.existsByIdAndEstado(id, "ACTIVO");
    }

    private void validatePrecio(PrecioRequest request) {
        if (request.getValor() == null || request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("El valor del precio debe ser mayor a cero");
        }

        if (request.getDescripcion() == null || request.getDescripcion().isBlank()) {
            throw new BusinessRuleException("La descripción del precio es obligatoria");
        }

        if (request.getEstado() != null) {
            validateEstado(request.getEstado());
        }
    }

    private void validateEstado(String estado) {
        if (!estado.equals("ACTIVO") && !estado.equals("INACTIVO")) {
            throw new BusinessRuleException("Estado de precio no válido");
        }
    }

    private PrecioResponse toResponse(Precio precio) {
        return PrecioResponse.builder()
                .id(precio.getId())
                .canchaId(precio.getCanchaId())
                .valor(precio.getValor())
                .moneda(precio.getMoneda())
                .descripcion(precio.getDescripcion())
                .estado(precio.getEstado())
                .createdAt(precio.getCreatedAt())
                .updatedAt(precio.getUpdatedAt())
                .build();
    }
}