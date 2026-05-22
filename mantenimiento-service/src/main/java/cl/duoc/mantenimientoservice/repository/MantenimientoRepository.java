package cl.duoc.mantenimientoservice.repository;

import cl.duoc.mantenimientoservice.model.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    List<Mantenimiento> findByCanchaIdAndEstadoNot(Long canchaId, String estado);

    boolean existsByIdAndEstadoNot(Long id, String estado);
}