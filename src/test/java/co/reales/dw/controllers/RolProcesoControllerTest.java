package co.reales.dw.controllers;

import co.reales.dw.dtos.RolProcesoDTO;
import co.reales.dw.services.RolProcesoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolProcesoControllerTest {

    private RolProcesoService rolProcesoService;
    private RolProcesoController rolProcesoController;

    @BeforeEach
    void setUp() {
        rolProcesoService = Mockito.mock(RolProcesoService.class);
        rolProcesoController = new RolProcesoController(rolProcesoService);
    }

    @Test
    void listar_ok() {
        RolProcesoDTO dto = new RolProcesoDTO();
        when(rolProcesoService.listarPorEmpresa(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<RolProcesoDTO>> response = rolProcesoController.listar(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(rolProcesoService).listarPorEmpresa(1L);
    }

    @Test
    void obtener_ok() {
        RolProcesoDTO dto = new RolProcesoDTO();
        when(rolProcesoService.obtenerRol(1L)).thenReturn(dto);

        ResponseEntity<RolProcesoDTO> response = rolProcesoController.obtener(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(rolProcesoService).obtenerRol(1L);
    }

    @Test
    void crear_ok() {
        RolProcesoDTO dto = new RolProcesoDTO();
        when(rolProcesoService.crearRol(dto)).thenReturn(dto);

        ResponseEntity<RolProcesoDTO> response = rolProcesoController.crear(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(rolProcesoService).crearRol(dto);
    }

    @Test
    void actualizar_ok() {
        RolProcesoDTO dto = new RolProcesoDTO();
        when(rolProcesoService.actualizarRol(1L, dto)).thenReturn(dto);

        ResponseEntity<RolProcesoDTO> response = rolProcesoController.actualizar(1L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(rolProcesoService).actualizarRol(1L, dto);
    }

    @Test
    void eliminar_ok() {
        ResponseEntity<Void> response = rolProcesoController.eliminar(1L);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(rolProcesoService).eliminarRol(1L);
    }
}