package co.reales.dw.repositories;

import co.reales.dw.entities.Arco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArcoRepository extends JpaRepository<Arco, Long> {
    List<Arco> findByProcesoId(Long procesoId);
    List<Arco> findByActividadOrigenId(Long actividadId);
    List<Arco> findByActividadDestinoId(Long actividadId);
}
