package cl.duoc.reservaservice.repository;

import cl.duoc.reservaservice.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByCanchaIdAndHorarioIdAndFechaReservaAndEstadoNot(
            Long canchaId,
            Long horarioId,
            LocalDateTime fechaReserva,
            String estado
    );

    boolean existsByIdAndEstadoNot(Long id, String estado);
}