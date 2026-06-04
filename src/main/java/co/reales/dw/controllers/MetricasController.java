package co.reales.dw.controllers;

import co.reales.dw.dtos.MetricasDTO;
import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.ProcesoRepository;
import co.reales.dw.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/metricas")
public class MetricasController {

    private final UsuarioRepository usuarioRepository;
    private final ProcesoRepository procesoRepository;
    private final ModelMapper modelMapper;

    public MetricasController(UsuarioRepository usuarioRepository,
                               ProcesoRepository procesoRepository,
                               ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.procesoRepository = procesoRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<MetricasDTO> getMetricas() {
        MetricasDTO metricas = new MetricasDTO();

        metricas.setTotalUsuarios(usuarioRepository.count());
        metricas.setTotalProcesos(procesoRepository.count());

        metricas.setProcesosPorEstado(Map.of(
                "BORRADOR", procesoRepository.countByEstado(Proceso.EstadoProceso.BORRADOR),
                "PUBLICADO", procesoRepository.countByEstado(Proceso.EstadoProceso.PUBLICADO)
        ));

        List<ProcesoDTO> ultimos = procesoRepository.findTop5ByOrderByIdDesc()
                .stream()
                .map(p -> modelMapper.map(p, ProcesoDTO.class))
                .toList();
        metricas.setUltimasModificaciones(ultimos);

        return ResponseEntity.ok(metricas);
    }
}
