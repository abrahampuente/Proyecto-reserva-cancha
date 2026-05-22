package cl.duoc.pagoservice.service;

import cl.duoc.pagoservice.client.ReservaClient;
import cl.duoc.pagoservice.dto.PagoRequest;
import cl.duoc.pagoservice.dto.PagoResponse;
import cl.duoc.pagoservice.exception.BusinessRuleException;
import cl.duoc.pagoservice.exception.ResourceNotFoundException;
import cl.duoc.pagoservice.model.Pago;
import cl.duoc.pagoservice.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaClient reservaClient;

    public PagoResponse create(PagoRequest request) {
        log.info("Creando pago para reservaId={}", request.getReservaId());

        reservaClient.validateReservaExists(request.getReservaId());
        validatePago(request);

        if (pagoRepository.existsByReservaIdAndEstado(request.getReservaId(), "PAGADO")) {
            throw new BusinessRuleException("La reserva ya tiene un pago registrado");
        }

        Pago pago = Pago.builder()
                .reservaId(request.getReservaId())
                .monto(request.getMonto())
                .moneda("CLP")
                .estado(request.getEstado() != null ? request.getEstado() : "PAGADO")
                .metodoPago(request.getMetodoPago())
                .codigoTransaccion(request.getCodigoTransaccion())
                .build();

        Pago saved = pagoRepository.save(pago);
        log.info("Pago creado con id={}", saved.getId());

        return toResponse(saved);
    }

    public List<PagoResponse> getAll() {
        log.info("Obteniendo todos los pagos");
        return pagoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PagoResponse getById(Long id) {
        log.info("Buscando pago con id={}", id);

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));

        return toResponse(pago);
    }

    public PagoResponse update(Long id, PagoRequest request) {
        log.info("Actualizando pago con id={}", id);

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));

        reservaClient.validateReservaExists(request.getReservaId());
        validatePago(request);

        pago.setReservaId(request.getReservaId());
        pago.setMonto(request.getMonto());
        pago.setMoneda("CLP");
        pago.setEstado(request.getEstado() != null ? request.getEstado() : pago.getEstado());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setCodigoTransaccion(request.getCodigoTransaccion());

        Pago updated = pagoRepository.save(pago);
        log.info("Pago actualizado con id={}", updated.getId());

        return toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Anulando pago con id={}", id);

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));

        pago.setEstado("ANULADO");
        pagoRepository.save(pago);

        log.info("Pago anulado con id={}", id);
    }

    public boolean existsById(Long id) {
        return pagoRepository.existsByIdAndEstadoNot(id, "ANULADO");
    }

    private void validatePago(PagoRequest request) {
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("El monto del pago debe ser mayor a cero");
        }

        if (request.getMetodoPago() == null || request.getMetodoPago().isBlank()) {
            throw new BusinessRuleException("El método de pago es obligatorio");
        }

        if (!request.getMetodoPago().equals("TARJETA")
                && !request.getMetodoPago().equals("TRANSFERENCIA")
                && !request.getMetodoPago().equals("WEBPAY")
                && !request.getMetodoPago().equals("EFECTIVO")) {
            throw new BusinessRuleException("Método de pago no válido");
        }

        String estado = request.getEstado();

        if (estado != null &&
                !estado.equals("PENDIENTE") &&
                !estado.equals("PAGADO") &&
                !estado.equals("RECHAZADO") &&
                !estado.equals("ANULADO")) {
            throw new BusinessRuleException("Estado de pago no válido");
        }
    }

    private PagoResponse toResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .reservaId(pago.getReservaId())
                .monto(pago.getMonto())
                .moneda(pago.getMoneda())
                .estado(pago.getEstado())
                .metodoPago(pago.getMetodoPago())
                .codigoTransaccion(pago.getCodigoTransaccion())
                .fechaPago(pago.getFechaPago())
                .createdAt(pago.getCreatedAt())
                .updatedAt(pago.getUpdatedAt())
                .build();
    }
}