package co.reales.dw.controllers;

import co.reales.dw.dtos.LoginRequestDTO;
import co.reales.dw.dtos.LoginResponseDTO;
import co.reales.dw.dtos.UsuarioRequestDTO;
import co.reales.dw.entities.Usuario;
import co.reales.dw.repositories.UsuarioRepository;
import co.reales.dw.services.AuthService;
import co.reales.dw.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private AuthService authService;
    @Mock private UsuarioService usuarioService;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks private AuthController authController;

    @Test
    void login_retornaTokenCorrecto() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setCorreo("test@test.com");
        request.setContrasena("pass123");

        LoginResponseDTO expected = new LoginResponseDTO("jwt-token", 1L, "test@test.com", "Test", "EDITOR", 1L);

        when(authService.login(request)).thenReturn(expected);

        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("jwt-token", response.getBody().getToken());
    }

    @Test
    void register_creaUsuarioYRetornaToken() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setCorreo("nuevo@test.com");
        dto.setContrasena("pass123");
        dto.setNombre("Nuevo");
        dto.setRol("EDITOR");
        dto.setEmpresaId(1L);

        LoginResponseDTO loginResp = new LoginResponseDTO("new-token", 2L, "nuevo@test.com", "Nuevo", "EDITOR", 1L);

        when(usuarioService.crearUsuario(any(UsuarioRequestDTO.class), eq(false))).thenReturn(null);
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(loginResp);

        ResponseEntity<LoginResponseDTO> response = authController.register(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("new-token", response.getBody().getToken());
        verify(usuarioService).crearUsuario(dto, false);
    }

    @Test
    void fixPassword_actualizaContrasenaCorrectamente() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setCorreo("user@test.com");
        request.setContrasena("newpass");

        Usuario usuario = new Usuario();
        usuario.setCorreo("user@test.com");
        usuario.setContrasena("oldpass");

        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(usuario);

        ResponseEntity<String> response = authController.fixPassword(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Contraseña actualizada correctamente", response.getBody());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void fixPassword_usuarioNoEncontradoLanzaExcepcion() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setCorreo("noexiste@test.com");
        request.setContrasena("pass");

        when(usuarioRepository.findByCorreo("noexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authController.fixPassword(request));
    }
}
