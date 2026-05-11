package cl.duoc.recintoservice.service;

import cl.duoc.recintoservice.dto.RecintoRequest;
import cl.duoc.recintoservice.dto.RecintoResponse;
import cl.duoc.recintoservice.exception.ResourceNotFoundException;
import cl.duoc.recintoservice.model.Recinto;
import cl.duoc.recintoservice.model.ImagenRecinto;
import cl.duoc.recintoservice.repository.RecintoRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecintoService {

    private static final Logger logger =
            LoggerFactory.getLogger(RecintoService.class);

    private final RecintoRepository repository;

    public RecintoService(RecintoRepository repository) {
        this.repository = repository;
    }

    public RecintoResponse create(RecintoRequest request) {

        logger.info("Creando recinto {}", request.getName());

        Recinto recinto = new Recinto();

        recinto.setName(request.getName());
        recinto.setAddress(request.getAddress());
        recinto.setCity(request.getCity());
        recinto.setCommune(request.getCommune());
        recinto.setPhone(request.getPhone());
        recinto.setManagerUserId(request.getManagerUserId());
        recinto.setStatus("ACTIVO");
        recinto.setCreatedAt(LocalDateTime.now());
        recinto.setUpdatedAt(LocalDateTime.now());

        if (request.getImagenes() != null) {

            List<ImagenRecinto> lista = request.getImagenes()
                    .stream()
                    .map(img -> {
                        ImagenRecinto imagen = new ImagenRecinto();
                        imagen.setImageUrl(img.getImageUrl());
                        imagen.setDescription(img.getDescription());
                        imagen.setRecinto(recinto);
                        return imagen;
                    }).toList();

            recinto.setImagenes(lista);
        }

        Recinto saved = repository.save(recinto);

        return mapToResponse(saved);
    }

    public List<RecintoResponse> getAll() {

        logger.info("Listando todos los recintos");

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RecintoResponse getById(Long id) {

        Recinto recinto = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Recinto no encontrado con id {}", id);

                    return new ResourceNotFoundException(
                            "Recinto no encontrado con id: " + id);
                });

        return mapToResponse(recinto);
    }

    public RecintoResponse update(Long id, RecintoRequest request) {

        logger.info("Actualizando recinto con id {}", id);

        Recinto recinto = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Recinto no encontrado con id {}", id);

                    return new ResourceNotFoundException(
                            "Recinto no encontrado con id: " + id);
                });

        recinto.setName(request.getName());
        recinto.setAddress(request.getAddress());
        recinto.setCity(request.getCity());
        recinto.setCommune(request.getCommune());
        recinto.setPhone(request.getPhone());
        recinto.setManagerUserId(request.getManagerUserId());
        recinto.setUpdatedAt(LocalDateTime.now());

        Recinto updated = repository.save(recinto);

        return mapToResponse(updated);
    }

    public void delete(Long id) {

        logger.warn("Eliminando recinto con id {}", id);

        Recinto recinto = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Recinto no encontrado con id {}", id);

                    return new ResourceNotFoundException(
                            "Recinto no encontrado con id: " + id);
                });

        repository.delete(recinto);
    }

    private RecintoResponse mapToResponse(Recinto recinto) {

        List<String> imagenes = recinto.getImagenes() != null
                ? recinto.getImagenes()
                .stream()
                .map(ImagenRecinto::getImageUrl)
                .toList()
                : null;

        return new RecintoResponse(
                recinto.getId(),
                recinto.getName(),
                recinto.getAddress(),
                recinto.getCity(),
                recinto.getCommune(),
                recinto.getPhone(),
                recinto.getManagerUserId(),
                recinto.getStatus(),
                imagenes
        );
    }
}