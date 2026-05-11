package cl.duoc.canchaservice.service;

import cl.duoc.canchaservice.dto.CanchaRequest;
import cl.duoc.canchaservice.dto.CanchaResponse;
import cl.duoc.canchaservice.exception.ResourceNotFoundException;
import cl.duoc.canchaservice.model.Cancha;
import cl.duoc.canchaservice.model.CaracteristicaCancha;
import cl.duoc.canchaservice.repository.CanchaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CanchaService {

    private static final Logger logger =
            LoggerFactory.getLogger(CanchaService.class);
    private final CanchaRepository repository;

    public CanchaService(CanchaRepository repository) {
        this.repository = repository;
    }

    public CanchaResponse create(CanchaRequest request) {

        logger.info("Creando cancha {}", request.getName());

        Cancha cancha = new Cancha();

        cancha.setName(request.getName());
        cancha.setSportType(request.getSportType());
        cancha.setSurfaceType(request.getSurfaceType());
        cancha.setCapacity(request.getCapacity());
        cancha.setRecintoId(request.getRecintoId());
        cancha.setStatus("ACTIVA");
        cancha.setCreatedAt(LocalDateTime.now());
        cancha.setUpdatedAt(LocalDateTime.now());


        if (request.getCaracteristicas() != null) {

            List<CaracteristicaCancha> lista = request.getCaracteristicas()
                    .stream()
                    .map(car -> {
                        CaracteristicaCancha caracteristica = new CaracteristicaCancha();
                        caracteristica.setName(car.getName());
                        caracteristica.setCancha(cancha);
                        return caracteristica;
                    }).toList();

            cancha.setCaracteristicas(lista);
        }

        Cancha saved = repository.save(cancha);

        return mapToResponse(saved);
    }

    public List<CanchaResponse> getAll() {

        logger.info("Listando todas las canchas");

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CanchaResponse getById(Long id) {

        Cancha cancha = repository.findById(id)
                .orElseThrow(() -> {

                        logger.error("Cancha no encontrada con id {}", id);

                       return new ResourceNotFoundException(
                               "Cancha no encontrada con id: " + id);
                });

        return mapToResponse(cancha);
    }

    public CanchaResponse update(Long id, CanchaRequest request) {

        logger.info("Actualizando cancha con id {}", id);

        Cancha cancha = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Cancha no encontrada con id {}", id);

                    return new ResourceNotFoundException(
                                    "Cancha no encontrada con id: " + id);
                        });

        cancha.setName(request.getName());
        cancha.setSportType(request.getSportType());
        cancha.setSurfaceType(request.getSurfaceType());
        cancha.setCapacity(request.getCapacity());
        cancha.setRecintoId(request.getRecintoId());
        cancha.setStatus(request.getStatus());
        cancha.setUpdatedAt(LocalDateTime.now());

        Cancha updated = repository.save(cancha);

        return mapToResponse(updated);
    }

    public void delete(Long id) {

        logger.warn("Eliminando cancha con id {}", id);

        Cancha cancha = repository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Cancha no encontrada con id {}", id);

                    return new ResourceNotFoundException(
                            "Cancha no encontrada con id: " + id);
                });

        repository.delete(cancha);
    }

    private CanchaResponse mapToResponse(Cancha cancha) {

        List<String> caracteristicas = cancha.getCaracteristicas() != null
                ? cancha.getCaracteristicas()
                .stream()
                .map(CaracteristicaCancha::getName)
                .toList()
                : null;

        return new CanchaResponse(
                cancha.getId(),
                cancha.getName(),
                cancha.getSportType(),
                cancha.getSurfaceType(),
                cancha.getCapacity(),
                cancha.getRecintoId(),
                cancha.getStatus(),
                caracteristicas
        );
    }
}