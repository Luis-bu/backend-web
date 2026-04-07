package co.reales.dw.controllers;

import co.reales.dw.dtos.UsuarioDTO;
import co.reales.dw.dtos.UsuarioRequestDTO;
import co.reales.dw.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    private UsuarioService usuarioService;
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        usuarioService = Mockito.mock(UsuarioService.class);
        usuarioController = new UsuarioController(usuarioService);
    }

    @Test
    void listar_ok() {
        UsuarioDTO dto = new UsuarioDTO();
        when(usuarioService.listarUsuariosPorEmpresa(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<UsuarioDTO>> response = usuarioController.listar(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(usuarioService).listarUsuariosPorEmpresa(1L);
    }

    @Test
    void obtener_ok() {
        UsuarioDTO dto = new UsuarioDTO();
        when(usuarioService.obtenerUsuario(1L)).thenReturn(dto);

        ResponseEntity<UsuarioDTO> response = usuarioController.obtener(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(usuarioService).obtenerUsuario(1L);
    }

    @Test
    void crear_ok() {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();
        UsuarioDTO responseDTO = new UsuarioDTO();

        when(usuarioService.crearUsuario(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.crear(requestDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(responseDTO, response.getBody());
        verify(usuarioService).crearUsuario(requestDTO);
    }

    @Test
    void actualizar_ok() {
        UsuarioDTO dto = new UsuarioDTO();
        when(usuarioService.actualizarUsuario(1L, dto)).thenReturn(dto);

        ResponseEntity<UsuarioDTO> response = usuarioController.actualizar(1L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
        verify(usuarioService).actualizarUsuario(1L, dto);
    }

    @Test
    void eliminar_ok() {
        ResponseEntity<Void> response = usuarioController.eliminar(1L);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(usuarioService).eliminarUsuario(1L);
    }
}