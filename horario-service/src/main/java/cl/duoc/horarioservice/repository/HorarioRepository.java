package cl.duoc.horarioservice.repository;

import cl.duoc.horarioservice.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByCanchaIdAndDayOfWeekAndStatus(
            Long canchaId,
            String dayOfWeek,
            String status
    );

    boolean existsByIdAndStatus(Long id, String status);
}