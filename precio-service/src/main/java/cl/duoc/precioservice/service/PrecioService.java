package cl.duoc.precioservice.service;

import cl.duoc.precioservice.dto.PrecioRequest;
import cl.duoc.precioservice.dto.PrecioResponse;
import cl.duoc.precioservice.exception.ResourceNotFoundException;
import cl.duoc.precioservice.model.Precio;
import cl.duoc.precioservice.repository.PrecioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrecioService {

    private final PrecioRepository precioRepository;

    public PrecioResponse create(PrecioRequest request) {
        log.info("Creando precio para canchaId={}", request.getCanchaId());
        Precio precio = Precio.builder()
                .canchaId(request.getCanchaId())
                .valor(request.getValor())
                .descripcion(request.getDescripcion())
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Precio no encontrado con id: " + id));
        return toResponse(precio);
    }

    public PrecioResponse update(Long id, PrecioRequest request) {
        log.info("Actualizando precio con id={}", id);
        Precio precio = precioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Precio no encontrado con id: " + id));
        precio.setCanchaId(request.getCanchaId());
        precio.setValor(request.getValor());
        precio.setDescripcion(request.getDescripcion());
        Precio updated = precioRepository.save(precio);
        log.info("Precio actualizado con id={}", updated.getId());
        return toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Eliminando precio con id={}", id);
        Precio precio = precioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Precio no encontrado con id: " + id));
        precioRepository.delete(precio);
        log.info("Precio eliminado con id={}", id);
    }

    private PrecioResponse toResponse(Precio precio) {
        return PrecioResponse.builder()
                .id(precio.getId())
                .canchaId(precio.getCanchaId())
                .valor(precio.getValor())
                .descripcion(precio.getDescripcion())
                .build();
    }
}