package co.reales.dw.repositories;

import co.reales.dw.entities.Proceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcesoRepository extends JpaRepository<Proceso, Long> {
    List<Proceso> findByEmpresaId(Long empresaId);
    List<Proceso> findByEmpresaIdAndEstado(Long empresaId, Proceso.EstadoProceso estado);
    List<Proceso> findByEmpresaIdAndCategoria(Long empresaId, String categoria);
}
