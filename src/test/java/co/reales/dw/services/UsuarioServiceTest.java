package co.reales.dw.services;

import co.reales.dw.dtos.UsuarioDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Usuario;
import co.reales.dw.repositories.EmpresaRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    //Listar por empresa
    @Test
    void testListarUsuariosPorEmpresa() {
        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();

        when(usuarioRepository.findByEmpresaId(1L))
                .thenReturn(List.of(usuario));

        when(modelMapper.map(usuario, UsuarioDTO.class))
                .thenReturn(dto);

        List<UsuarioDTO> result = usuarioService.listarUsuariosPorEmpresa(1L);

        assertEquals(1, result.size());
        verify(usuarioRepository).findByEmpresaId(1L);
    }

    // Obtener usuario por ID
    @Test
    void testObtenerUsuario_ok() {
        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(modelMapper.map(usuario, UsuarioDTO.class))
                .thenReturn(dto);

        UsuarioDTO result = usuarioService.obtenerUsuario(1L);

        assertNotNull(result);
    }

    // Error al obtener usuario por ID
    @Test
    void testObtenerUsuario_notFound() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerUsuario(1L);
        });
    }

    // Crear usuario
    @Test
    void testCrearUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmpresaId(1L);

        Usuario usuario = new Usuario();
        Empresa empresa = new Empresa();

        when(empresaRepository.findById(1L))
                .thenReturn(Optional.of(empresa));

        when(modelMapper.map(dto, Usuario.class))
                .thenReturn(usuario);

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        when(modelMapper.map(usuario, UsuarioDTO.class))
                .thenReturn(dto);

        UsuarioDTO result = usuarioService.crearUsuario(dto);

        assertNotNull(result);
        verify(usuarioRepository).save(usuario);
    }

    // Crear usuario con empresa no encontrada
    @Test
    void testCrearUsuario_empresaNotFound() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmpresaId(1L);

        when(empresaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(dto);
        });
    }

    // Actualizar usuario
    @Test
    void testActualizarUsuario() {
        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();

        dto.setNombre("Nuevo");
        dto.setCorreo("correo@test.com");
        dto.setRol("ADMINISTRADOR");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        when(modelMapper.map(usuario, UsuarioDTO.class))
                .thenReturn(dto);

        UsuarioDTO result = usuarioService.actualizarUsuario(1L, dto);

        assertEquals("Nuevo", result.getNombre());
    }

    // Actualizar usuario no encontrado
    @Test
    void testActualizarUsuario_notFound() {
        UsuarioDTO dto = new UsuarioDTO();

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario(1L, dto);
        });
    }

    // Eliminar usuario
    @Test
    void testEliminarUsuario() {
        usuarioService.eliminarUsuario(1L);
        verify(usuarioRepository).deleteById(1L);
    }
}