package cl.duoc.precioservice.repository;

import cl.duoc.precioservice.model.Precio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long> {

    boolean existsByCanchaIdAndEstado(Long canchaId, String estado);

    boolean existsByIdAndEstado(Long id, String estado);
}