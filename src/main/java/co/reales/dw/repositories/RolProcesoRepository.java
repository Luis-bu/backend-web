package co.reales.dw.repositories;

import co.reales.dw.entities.RolProceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RolProcesoRepository extends JpaRepository<RolProceso, Long> {
    List<RolProceso> findByEmpresaId(Long empresaId);
    boolean existsByIdAndActividadesIsNotEmpty(Long id);
}
