package co.reales.dw.controllers;

import co.reales.dw.dtos.ActividadDTO;
import co.reales.dw.services.ActividadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActividadControllerTest {

    private ActividadService actividadService;
    private ActividadController actividadController;

    @BeforeEach
    void setUp() {
        actividadService = Mockito.mock(ActividadService.class);
        actividadController = new ActividadController(actividadService);
    }

    @Test
    void listar_ok() {
        ActividadDTO dto = new ActividadDTO();
        when(actividadService.listarPorProceso(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<ActividadDTO>> response = actividadController.listar(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(actividadService).listarPorProceso(1L);
    }

    @Test
    void obtener_ok() {
        ActividadDTO dto = new ActividadDTO();
        when(actividadService.obtenerActividad(1L)).thenReturn(dto);

        ResponseEntity<ActividadDTO> response = actividadController.obtener(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(actividadService).obtenerActividad(1L);
    }

    @Test
    void crear_ok() {
        ActividadDTO dto = new ActividadDTO();
        when(actividadService.crearActividad(dto)).thenReturn(dto);

        ResponseEntity<ActividadDTO> response = actividadController.crear(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(actividadService).crearActividad(dto);
    }

    @Test
    void actualizar_ok() {
        ActividadDTO dto = new ActividadDTO();
        when(actividadService.actualizarActividad(1L, dto)).thenReturn(dto);

        ResponseEntity<ActividadDTO> response = actividadController.actualizar(1L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(actividadService).actualizarActividad(1L, dto);
    }

    @Test
    void eliminar_ok() {
        ResponseEntity<Void> response = actividadController.eliminar(1L);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(actividadService).eliminarActividad(1L);
    }
}