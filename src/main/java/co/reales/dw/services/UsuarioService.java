package co.reales.dw.services;

import co.reales.dw.dtos.UsuarioDTO;
import co.reales.dw.dtos.UsuarioRequestDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Usuario;
import co.reales.dw.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaService empresaService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository, EmpresaService empresaService, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.empresaService = empresaService;
        this.modelMapper = modelMapper;
    }

    public List<UsuarioDTO> listarUsuariosPorEmpresa(Long empresaId) {
        return usuarioRepository.findByEmpresaId(empresaId)
                .stream()
                .map(u -> modelMapper.map(u, UsuarioDTO.class))
                .toList();
    }

    public UsuarioDTO obtenerUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    public UsuarioDTO crearUsuario(UsuarioRequestDTO dto) {
        Empresa empresa = modelMapper.map(
            empresaService.obtenerEmpresa(dto.getEmpresaId()), Empresa.class);
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setRol(Usuario.RolUsuario.valueOf(dto.getRol()));
        usuario.setEmpresa(empresa);
        return modelMapper.map(usuarioRepository.save(usuario), UsuarioDTO.class);
    }

    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setRol(Usuario.RolUsuario.valueOf(dto.getRol()));
        return modelMapper.map(usuarioRepository.save(usuario), UsuarioDTO.class);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}