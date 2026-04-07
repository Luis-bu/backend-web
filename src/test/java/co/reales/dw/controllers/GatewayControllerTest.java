package co.reales.dw.controllers;

import co.reales.dw.dtos.GatewayDTO;
import co.reales.dw.services.GatewayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GatewayControllerTest {

    private GatewayService gatewayService;
    private GatewayController gatewayController;

    @BeforeEach
    void setUp() {
        gatewayService = Mockito.mock(GatewayService.class);
        gatewayController = new GatewayController(gatewayService);
    }

    @Test
    void listar_ok() {
        GatewayDTO dto = new GatewayDTO();
        when(gatewayService.listarPorProceso(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<GatewayDTO>> response = gatewayController.listar(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(gatewayService).listarPorProceso(1L);
    }

    @Test
    void obtener_ok() {
        GatewayDTO dto = new GatewayDTO();
        when(gatewayService.obtenerGateway(1L)).thenReturn(dto);

        ResponseEntity<GatewayDTO> response = gatewayController.obtener(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(gatewayService).obtenerGateway(1L);
    }

    @Test
    void crear_ok() {
        GatewayDTO dto = new GatewayDTO();
        when(gatewayService.crearGateway(dto)).thenReturn(dto);

        ResponseEntity<GatewayDTO> response = gatewayController.crear(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(gatewayService).crearGateway(dto);
    }

    @Test
    void actualizar_ok() {
        GatewayDTO dto = new GatewayDTO();
        when(gatewayService.actualizarGateway(1L, dto)).thenReturn(dto);

        ResponseEntity<GatewayDTO> response = gatewayController.actualizar(1L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(gatewayService).actualizarGateway(1L, dto);
    }

    @Test
    void eliminar_ok() {
        ResponseEntity<Void> response = gatewayController.eliminar(1L);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(gatewayService).eliminarGateway(1L);
    }
}