package co.reales.dw.services;

import co.reales.dw.dtos.UsuarioDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Usuario;
import co.reales.dw.repositories.EmpresaRepository;
import co.reales.dw.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<UsuarioDTO> listarUsuariosPorEmpresa(Long empresaId) {
        return usuarioRepository.findByEmpresaId(empresaId)
                .stream()
                .map(u -> modelMapper.map(u, UsuarioDTO.class))
                .collect(Collectors.toList());
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