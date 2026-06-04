package co.reales.dw.controllers;

import co.reales.dw.dtos.MetricasDTO;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.ProcesoRepository;
import co.reales.dw.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricasControllerTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ProcesoRepository procesoRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks private MetricasController metricasController;

    @Test
    void getMetricas_retornaTodosLosCampos() {
        when(usuarioRepository.count()).thenReturn(8L);
        when(procesoRepository.count()).thenReturn(15L);
        when(procesoRepository.countByEstado(Proceso.EstadoProceso.BORRADOR)).thenReturn(5L);
        when(procesoRepository.countByEstado(Proceso.EstadoProceso.PUBLICADO)).thenReturn(10L);
        when(procesoRepository.findTop5ByOrderByIdDesc()).thenReturn(List.of());

        ResponseEntity<MetricasDTO> response = metricasController.getMetricas();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        MetricasDTO body = response.getBody();
        assertEquals(8L, body.getTotalUsuarios());
        assertEquals(15L, body.getTotalProcesos());
        assertEquals(5L, body.getProcesosPorEstado().get("BORRADOR"));
        assertEquals(10L, body.getProcesosPorEstado().get("PUBLICADO"));
        assertNotNull(body.getUltimasModificaciones());
    }

    @Test
    void getMetricas_sinProcesos_retornaCeros() {
        when(usuarioRepository.count()).thenReturn(0L);
        when(procesoRepository.count()).thenReturn(0L);
        when(procesoRepository.countByEstado(Proceso.EstadoProceso.BORRADOR)).thenReturn(0L);
        when(procesoRepository.countByEstado(Proceso.EstadoProceso.PUBLICADO)).thenReturn(0L);
        when(procesoRepository.findTop5ByOrderByIdDesc()).thenReturn(List.of());

        ResponseEntity<MetricasDTO> response = metricasController.getMetricas();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(0L, response.getBody().getTotalProcesos());
        assertEquals(0L, response.getBody().getTotalUsuarios());
    }

    @Test
    void getMetricas_ultimas5ModificacionesIncluidas() {
        Proceso p1 = new Proceso(); p1.setId(10L);
        Proceso p2 = new Proceso(); p2.setId(9L);

        when(usuarioRepository.count()).thenReturn(3L);
        when(procesoRepository.count()).thenReturn(2L);
        when(procesoRepository.countByEstado(any())).thenReturn(1L);
        when(procesoRepository.findTop5ByOrderByIdDesc()).thenReturn(List.of(p1, p2));
        when(modelMapper.map(any(), any())).thenReturn(new co.reales.dw.dtos.ProcesoDTO());

        ResponseEntity<MetricasDTO> response = metricasController.getMetricas();

        assertEquals(2, response.getBody().getUltimasModificaciones().size());
    }
}
