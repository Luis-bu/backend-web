package co.reales.dw.controllers;

import co.reales.dw.dtos.EmpresaDTO;
import co.reales.dw.services.EmpresaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpresaControllerTest {

    private EmpresaService empresaService;
    private EmpresaController empresaController;

    @BeforeEach
    void setUp() {
        empresaService = Mockito.mock(EmpresaService.class);
        empresaController = new EmpresaController(empresaService);
    }

    @Test
    void listar_ok() {
        EmpresaDTO dto = new EmpresaDTO();
        when(empresaService.listarEmpresas()).thenReturn(List.of(dto));

        ResponseEntity<List<EmpresaDTO>> response = empresaController.listar();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(empresaService).listarEmpresas();
    }

    @Test
    void obtener_ok() {
        EmpresaDTO dto = new EmpresaDTO();
        when(empresaService.obtenerEmpresa(1L)).thenReturn(dto);

        ResponseEntity<EmpresaDTO> response = empresaController.obtener(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(empresaService).obtenerEmpresa(1L);
    }

    @Test
    void crear_ok() {
        EmpresaDTO dto = new EmpresaDTO();
        when(empresaService.crearEmpresa(dto)).thenReturn(dto);

        ResponseEntity<EmpresaDTO> response = empresaController.crear(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(empresaService).crearEmpresa(dto);
    }

    @Test
    void actualizar_ok() {
        EmpresaDTO dto = new EmpresaDTO();
        when(empresaService.actualizarEmpresa(1L, dto)).thenReturn(dto);

        ResponseEntity<EmpresaDTO> response = empresaController.actualizar(1L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(empresaService).actualizarEmpresa(1L, dto);
    }

    @Test
    void eliminar_ok() {
        ResponseEntity<Void> response = empresaController.eliminar(1L);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(empresaService).eliminarEmpresa(1L);
    }
}
