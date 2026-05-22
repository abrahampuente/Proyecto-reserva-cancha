package cl.duoc.recintoservice.repository;

import cl.duoc.recintoservice.model.Recinto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecintoRepository extends JpaRepository<Recinto, Long> {

    boolean existsByIdAndStatus(Long id, String status);

    boolean existsByNameAndAddressAndCommuneAndStatus(
            String name,
            String address,
            String commune,
            String status
    );
}