package cl.duoc.canchaservice.repository;

import cl.duoc.canchaservice.model.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {

    boolean existsByIdAndStatus(Long id, String status);

    boolean existsByNameAndRecintoIdAndStatus(
            String name,
            Long recintoId,
            String status
    );
}