package co.reales.dw.services;

import co.reales.dw.dtos.LoginRequestDTO;
import co.reales.dw.dtos.LoginResponseDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Usuario;
import co.reales.dw.exceptions.BadRequestException;
import co.reales.dw.repositories.UsuarioRepository;
import co.reales.dw.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        Empresa empresa = new Empresa();
        empresa.setId(1L);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("admin@test.com");
        usuario.setContrasena(encoder.encode("password123"));
        usuario.setRol(Usuario.RolUsuario.ADMINISTRADOR);
        usuario.setNombre("Admin");
        usuario.setEmpresa(empresa);
    }

    @Test
    void testLoginExitoso() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setCorreo("admin@test.com");
        request.setContrasena("password123");

        when(usuarioRepository.findByCorreo("admin@test.com")).thenReturn(Optional.of(usuario));
        when(jwtUtil.generateToken(1L, "admin@test.com", 1L, "ADMINISTRADOR"))
                .thenReturn("mock.jwt.token");

        LoginResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("admin@test.com", response.getCorreo());
        assertEquals(1L, response.getEmpresaId());
    }

    @Test
    void testLoginCorreoInexistente() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setCorreo("noexiste@test.com");
        request.setContrasena("password123");

        when(usuarioRepository.findByCorreo("noexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> authService.login(request));
    }

    @Test
    void testLoginContrasenaIncorrecta() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setCorreo("admin@test.com");
        request.setContrasena("wrongpassword");

        when(usuarioRepository.findByCorreo("admin@test.com")).thenReturn(Optional.of(usuario));

        assertThrows(BadRequestException.class, () -> authService.login(request));
    }

    @Test
    void testLoginRetornaEmpresaId() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setCorreo("admin@test.com");
        request.setContrasena("password123");

        when(usuarioRepository.findByCorreo("admin@test.com")).thenReturn(Optional.of(usuario));
        when(jwtUtil.generateToken(anyLong(), anyString(), anyLong(), anyString()))
                .thenReturn("token");

        LoginResponseDTO response = authService.login(request);

        assertEquals(1L, response.getEmpresaId());
        assertEquals("ADMINISTRADOR", response.getRol());
    }
}
