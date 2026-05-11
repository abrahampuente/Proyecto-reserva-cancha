package cl.duoc.horarioservice.repository;

import cl.duoc.horarioservice.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

}