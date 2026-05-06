package cl.duoc.recintoservice.service;

import cl.duoc.recintoservice.dto.RecintoRequest;
import cl.duoc.recintoservice.dto.RecintoResponse;
import cl.duoc.recintoservice.exception.ResourceNotFoundException;
import cl.duoc.recintoservice.model.Recinto;
import cl.duoc.recintoservice.model.ImagenRecinto;
import cl.duoc.recintoservice.repository.RecintoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecintoService {

    private final RecintoRepository repository;

    public RecintoService(RecintoRepository repository) {
        this.repository = repository;
    }

    public RecintoResponse create(RecintoRequest request) {
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

        // 👉 Agregar imágenes
        if (request.getImagenes() != null) {
            List<ImagenRecinto> lista = request.getImagenes().stream().map(img -> {
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
        Recinto recinto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recinto no encontrado con id: " + id));

        repository.delete(recinto);
    }

    private RecintoResponse mapToResponse(Recinto recinto) {

        List<String> imagenes = recinto.getImagenes() != null
                ? recinto.getImagenes().stream()
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