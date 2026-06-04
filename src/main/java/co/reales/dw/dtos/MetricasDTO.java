package co.reales.dw.dtos;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class MetricasDTO {
    private long totalUsuarios;
    private long totalProcesos;
    private Map<String, Long> procesosPorEstado;
    private List<ProcesoDTO> ultimasModificaciones;
}
