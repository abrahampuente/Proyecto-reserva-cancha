package cl.duoc.pagoservice.service;

import cl.duoc.pagoservice.dto.PagoRequest;
import cl.duoc.pagoservice.dto.PagoResponse;
import cl.duoc.pagoservice.exception.ResourceNotFoundException;
import cl.duoc.pagoservice.model.Pago;
import cl.duoc.pagoservice.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoResponse create(PagoRequest request) {
        log.info("Creando pago para reservaId={}", request.getReservaId());
        Pago pago = Pago.builder()
                .reservaId(request.getReservaId())
                .monto(request.getMonto())
                .estado(request.getEstado())
                .metodoPago(request.getMetodoPago())
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago no encontrado con id: " + id));
        return toResponse(pago);
    }

    public PagoResponse update(Long id, PagoRequest request) {
        log.info("Actualizando pago con id={}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago no encontrado con id: " + id));
        pago.setReservaId(request.getReservaId());
        pago.setMonto(request.getMonto());
        pago.setEstado(request.getEstado());
        pago.setMetodoPago(request.getMetodoPago());
        Pago updated = pagoRepository.save(pago);
        log.info("Pago actualizado con id={}", updated.getId());
        return toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Eliminando pago con id={}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago no encontrado con id: " + id));
        pagoRepository.delete(pago);
        log.info("Pago eliminado con id={}", id);
    }

    private PagoResponse toResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .reservaId(pago.getReservaId())
                .monto(pago.getMonto())
                .estado(pago.getEstado())
                .metodoPago(pago.getMetodoPago())
                .build();
    }
}