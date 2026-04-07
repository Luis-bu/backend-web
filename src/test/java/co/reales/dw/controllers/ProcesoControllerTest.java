package co.reales.dw.controllers;

import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.services.ProcesoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcesoControllerTest {

    private ProcesoService procesoService;
    private ProcesoController procesoController;

    @BeforeEach
    void setUp() {
        procesoService = Mockito.mock(ProcesoService.class);
        procesoController = new ProcesoController(procesoService);
    }

    @Test
    void listar_ok() {
        ProcesoDTO dto = new ProcesoDTO();
        when(procesoService.listarProcesosPorEmpresa(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<ProcesoDTO>> response = procesoController.listar(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(procesoService).listarProcesosPorEmpresa(1L);
    }

    @Test
    void obtener_ok() {
        ProcesoDTO dto = new ProcesoDTO();
        when(procesoService.obtenerProceso(1L)).thenReturn(dto);

        ResponseEntity<ProcesoDTO> response = procesoController.obtener(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(procesoService).obtenerProceso(1L);
    }

    @Test
    void crear_ok() {
        ProcesoDTO dto = new ProcesoDTO();
        when(procesoService.crearProceso(dto)).thenReturn(dto);

        ResponseEntity<ProcesoDTO> response = procesoController.crear(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(procesoService).crearProceso(dto);
    }

    @Test
    void actualizar_ok() {
        ProcesoDTO dto = new ProcesoDTO();
        when(procesoService.actualizarProceso(1L, dto)).thenReturn(dto);

        ResponseEntity<ProcesoDTO> response = procesoController.actualizar(1L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(procesoService).actualizarProceso(1L, dto);
    }

    @Test
    void eliminar_ok() {
        ResponseEntity<Void> response = procesoController.eliminar(1L);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(procesoService).eliminarProceso(1L);
    }
}
