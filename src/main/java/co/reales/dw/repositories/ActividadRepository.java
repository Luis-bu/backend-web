package co.reales.dw.repositories;

import co.reales.dw.entities.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByProcesoId(Long procesoId);
    List<Actividad> findByRolProcesoId(Long rolProcesoId);
}