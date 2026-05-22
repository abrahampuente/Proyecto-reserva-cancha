package cl.duoc.canchaservice.service;

import cl.duoc.canchaservice.client.RecintoClient;
import cl.duoc.canchaservice.dto.CanchaRequest;
import cl.duoc.canchaservice.dto.CanchaResponse;
import cl.duoc.canchaservice.exception.BusinessRuleException;
import cl.duoc.canchaservice.exception.ResourceNotFoundException;
import cl.duoc.canchaservice.model.Cancha;
import cl.duoc.canchaservice.model.CaracteristicaCancha;
import cl.duoc.canchaservice.repository.CanchaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CanchaService {

    private static final Logger logger = LoggerFactory.getLogger(CanchaService.class);

    private final CanchaRepository repository;
    private final RecintoClient recintoClient;

    public CanchaService(CanchaRepository repository, RecintoClient recintoClient) {
        this.repository = repository;
        this.recintoClient = recintoClient;
    }

    public CanchaResponse create(CanchaRequest request) {
        logger.info("Creando cancha {}", request.getName());

        recintoClient.validateRecintoExists(request.getRecintoId());
        validateCancha(request);

        if (repository.existsByNameAndRecintoIdAndStatus(
                request.getName(),
                request.getRecintoId(),
                "ACTIVA")) {
            throw new BusinessRuleException(
                    "Ya existe una cancha activa con ese nombre en el recinto"
            );
        }

        Cancha cancha = new Cancha();
        cancha.setName(request.getName());
        cancha.setSportType(request.getSportType());
        cancha.setSurfaceType(request.getSurfaceType());
        cancha.setCapacity(request.getCapacity());
        cancha.setRecintoId(request.getRecintoId());
        cancha.setStatus("ACTIVA");

        if (request.getCaracteristicas() != null) {
            List<CaracteristicaCancha> lista = request.getCaracteristicas()
                    .stream()
                    .map(car -> {
                        CaracteristicaCancha caracteristica = new CaracteristicaCancha();
                        caracteristica.setName(car.getName());
                        caracteristica.setCancha(cancha);
                        return caracteristica;
                    })
                    .toList();

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
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cancha no encontrada con id: " + id));

        return mapToResponse(cancha);
    }

    public CanchaResponse update(Long id, CanchaRequest request) {
        logger.info("Actualizando cancha {}", id);

        Cancha cancha = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cancha no encontrada con id: " + id));

        recintoClient.validateRecintoExists(request.getRecintoId());
        validateCancha(request);

        List<Cancha> duplicadas = repository.findAll()
                .stream()
                .filter(c ->
                        !c.getId().equals(id)
                                && c.getName().equalsIgnoreCase(request.getName())
                                && c.getRecintoId().equals(request.getRecintoId())
                                && "ACTIVA".equals(c.getStatus()))
                .toList();

        if (!duplicadas.isEmpty()) {
            throw new BusinessRuleException(
                    "Ya existe otra cancha activa con ese nombre en el recinto"
            );
        }

        cancha.setName(request.getName());
        cancha.setSportType(request.getSportType());
        cancha.setSurfaceType(request.getSurfaceType());
        cancha.setCapacity(request.getCapacity());
        cancha.setRecintoId(request.getRecintoId());
        cancha.setStatus(request.getStatus() != null ? request.getStatus() : cancha.getStatus());

        Cancha updated = repository.save(cancha);
        return mapToResponse(updated);
    }

    public void delete(Long id) {
        logger.warn("Desactivando cancha {}", id);

        Cancha cancha = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cancha no encontrada con id: " + id));

        cancha.setStatus("INACTIVA");
        repository.save(cancha);
    }

    public boolean existsById(Long id) {
        return repository.existsByIdAndStatus(id, "ACTIVA");
    }

    private void validateCancha(CanchaRequest request) {
        if (request.getCapacity() == null || request.getCapacity() <= 0) {
            throw new BusinessRuleException(
                    "La capacidad debe ser mayor a cero"
            );
        }
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