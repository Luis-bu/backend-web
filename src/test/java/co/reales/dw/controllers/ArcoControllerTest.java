package co.reales.dw.controllers;

import co.reales.dw.dtos.ArcoDTO;
import co.reales.dw.services.ArcoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArcoControllerTest {

    private ArcoService arcoService;
    private ArcoController arcoController;

    @BeforeEach
    void setUp() {
        arcoService = Mockito.mock(ArcoService.class);
        arcoController = new ArcoController(arcoService);
    }

    @Test
    void listar_ok() {
        ArcoDTO dto = new ArcoDTO();
        when(arcoService.listarPorProceso(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<ArcoDTO>> response = arcoController.listar(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(arcoService).listarPorProceso(1L);
    }

    @Test
    void obtener_ok() {
        ArcoDTO dto = new ArcoDTO();
        when(arcoService.obtenerArco(1L)).thenReturn(dto);

        ResponseEntity<ArcoDTO> response = arcoController.obtener(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(arcoService).obtenerArco(1L);
    }

    @Test
    void crear_ok() {
        ArcoDTO dto = new ArcoDTO();
        when(arcoService.crearArco(dto)).thenReturn(dto);

        ResponseEntity<ArcoDTO> response = arcoController.crear(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(arcoService).crearArco(dto);
    }

    @Test
    void actualizar_ok() {
        ArcoDTO dto = new ArcoDTO();
        when(arcoService.actualizarArco(1L, dto)).thenReturn(dto);

        ResponseEntity<ArcoDTO> response = arcoController.actualizar(1L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(arcoService).actualizarArco(1L, dto);
    }

    @Test
    void eliminar_ok() {
        ResponseEntity<Void> response = arcoController.eliminar(1L);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(arcoService).eliminarArco(1L);
    }
}