package cl.duoc.notificacionservice.repository;

import cl.duoc.notificacionservice.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    boolean existsByIdAndEstadoNot(Long id, String estado);
}