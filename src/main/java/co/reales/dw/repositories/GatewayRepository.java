package co.reales.dw.repositories;

import co.reales.dw.entities.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {
    List<Gateway> findByProcesoId(Long procesoId);
    List<Gateway> findByTipo(Gateway.TipoGateway tipo);
}