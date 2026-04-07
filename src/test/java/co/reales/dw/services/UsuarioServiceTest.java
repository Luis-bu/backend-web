package co.reales.dw.services;

import co.reales.dw.dtos.EmpresaDTO;
import co.reales.dw.dtos.UsuarioDTO;
import co.reales.dw.dtos.UsuarioRequestDTO;
import co.reales.dw.entities.Usuario;
import co.reales.dw.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testListarUsuariosPorEmpresa() {
        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();

        when(usuarioRepository.findByEmpresaId(1L)).thenReturn(List.of(usuario));
        when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(dto);

        List<UsuarioDTO> result = usuarioService.listarUsuariosPorEmpresa(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testObtenerUsuario_ok() {
        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(dto);

        UsuarioDTO result = usuarioService.obtenerUsuario(1L);

        assertNotNull(result);
    }

    @Test
    void testObtenerUsuario_notFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.obtenerUsuario(1L));
    }

    @Test
    void testCrearUsuario() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setEmpresaId(1L);
        dto.setContrasena("password123");
        dto.setRol("ADMINISTRADOR");

        Usuario usuario = new Usuario();
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        when(empresaService.obtenerEmpresa(1L)).thenReturn(new EmpresaDTO());
        when(modelMapper.map(any(), eq(co.reales.dw.entities.Empresa.class))).thenReturn(new co.reales.dw.entities.Empresa());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(usuarioDTO);

        UsuarioDTO result = usuarioService.crearUsuario(dto);

        assertNotNull(result);
    }

    @Test
void testCrearUsuario_empresaNotFound() {
    UsuarioRequestDTO dto = new UsuarioRequestDTO();
    dto.setEmpresaId(1L);
    dto.setContrasena("pass");
    dto.setRol("ADMINISTRADOR");

    when(empresaService.obtenerEmpresa(1L)).thenThrow(new RuntimeException("Empresa no encontrada"));

    assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(dto));
}

    @Test
    void testActualizarUsuario() {
        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Nuevo");
        dto.setCorreo("correo@test.com");
        dto.setRol("ADMINISTRADOR");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioDTO.class)).thenReturn(dto);

        UsuarioDTO result = usuarioService.actualizarUsuario(1L, dto);

        assertEquals("Nuevo", result.getNombre());
    }

    @Test
void testActualizarUsuario_notFound() {
    when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
    UsuarioDTO dto = new UsuarioDTO();
    assertThrows(RuntimeException.class, () -> usuarioService.actualizarUsuario(1L, dto));
}

    @Test
    void testEliminarUsuario() {
        usuarioService.eliminarUsuario(1L);
        verify(usuarioRepository).deleteById(1L);
    }
}