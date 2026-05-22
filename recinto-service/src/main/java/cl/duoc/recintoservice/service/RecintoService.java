package cl.duoc.recintoservice.service;

import cl.duoc.recintoservice.client.UserClient;
import cl.duoc.recintoservice.dto.RecintoRequest;
import cl.duoc.recintoservice.dto.RecintoResponse;
import cl.duoc.recintoservice.exception.ResourceNotFoundException;
import cl.duoc.recintoservice.model.ImagenRecinto;
import cl.duoc.recintoservice.model.Recinto;
import cl.duoc.recintoservice.repository.RecintoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import cl.duoc.recintoservice.exception.BusinessRuleException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecintoService {

    private static final Logger logger = LoggerFactory.getLogger(RecintoService.class);

    private final RecintoRepository repository;
    private final UserClient userClient;

    public RecintoService(RecintoRepository repository, UserClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }

    public RecintoResponse create(RecintoRequest request) {
        logger.info("Creando recinto {}", request.getName());

        userClient.validateOwnerOrAdmin(request.getManagerUserId());

        if (repository.existsByNameAndAddressAndCommuneAndStatus(
                request.getName(),
                request.getAddress(),
                request.getCommune(),
                "ACTIVO")) {
            throw new BusinessRuleException("Ya existe un recinto activo con el mismo nombre, dirección y comuna");
        }

        Recinto recinto = new Recinto();
        recinto.setName(request.getName());
        recinto.setAddress(request.getAddress());
        recinto.setCity(request.getCity());
        recinto.setCommune(request.getCommune());
        recinto.setPhone(request.getPhone());
        recinto.setManagerUserId(request.getManagerUserId());
        recinto.setStatus("ACTIVO");
        recinto.setImagenes(mapImagenes(request, recinto));

        Recinto saved = repository.save(recinto);
        return mapToResponse(saved);
    }

    public List<RecintoResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RecintoResponse getById(Long id) {
        Recinto recinto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recinto no encontrado con id: " + id));

        return mapToResponse(recinto);
    }

    public RecintoResponse update(Long id, RecintoRequest request) {
        Recinto recinto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recinto no encontrado con id: " + id));

        userClient.validateOwnerOrAdmin(request.getManagerUserId());

        recinto.setName(request.getName());
        recinto.setAddress(request.getAddress());
        recinto.setCity(request.getCity());
        recinto.setCommune(request.getCommune());
        recinto.setPhone(request.getPhone());
        recinto.setManagerUserId(request.getManagerUserId());

        if (recinto.getImagenes() != null) {
            recinto.getImagenes().clear();
            recinto.getImagenes().addAll(mapImagenes(request, recinto));
        } else {
            recinto.setImagenes(mapImagenes(request, recinto));
        }

        Recinto updated = repository.save(recinto);
        return mapToResponse(updated);
    }

    public void delete(Long id) {
        Recinto recinto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recinto no encontrado con id: " + id));

        recinto.setStatus("INACTIVO");
        repository.save(recinto);
    }

    public boolean existsById(Long id) {
        return repository.existsByIdAndStatus(id, "ACTIVO");
    }

    private List<ImagenRecinto> mapImagenes(RecintoRequest request, Recinto recinto) {
        if (request.getImagenes() == null) {
            return null;
        }

        return request.getImagenes()
                .stream()
                .map(img -> {
                    ImagenRecinto imagen = new ImagenRecinto();
                    imagen.setImageUrl(img.getImageUrl());
                    imagen.setDescription(img.getDescription());
                    imagen.setRecinto(recinto);
                    return imagen;
                })
                .toList();
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
                imagenes,
                recinto.getCreatedAt(),
                recinto.getUpdatedAt()
        );
    }
}