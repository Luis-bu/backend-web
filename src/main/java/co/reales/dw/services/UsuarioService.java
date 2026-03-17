package co.reales.dw.services;

import co.reales.dw.dtos.UsuarioDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Usuario;
import co.reales.dw.repositories.EmpresaRepository;
import co.reales.dw.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
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

    public UsuarioDTO crearUsuario(UsuarioDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        Usuario usuario = modelMapper.map(dto, Usuario.class);
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